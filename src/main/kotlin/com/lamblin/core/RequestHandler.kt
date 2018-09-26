package com.lamblin.core

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.core.JsonProcessingException
import com.lamblin.alerting.AlertDispatcher
import com.lamblin.alerting.LambdaAlert
import com.lamblin.alerting.SlackAlertDispatcher
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HandlerMethodComparator
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.StatusCode
import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger(RequestHandler::class.java)

internal class RequestHandler(
        private val endpointInvoker: EndpointInvoker,
        private val alertDispatcher: AlertDispatcher? = null) {

    companion object {
        internal fun noAlertingInstance(controllerRegistry: ControllerRegistry) = RequestHandler(EndpointInvoker(
                RequestToParamValueMapper.instance(),
                controllerRegistry))

        internal fun withAlerting(
                controllerRegistry: ControllerRegistry,
                slackDispatcherLambda: String) = RequestHandler(
                        EndpointInvoker(
                                RequestToParamValueMapper.instance(),
                                controllerRegistry),
                        SlackAlertDispatcher(slackDispatcherLambda))
    }

    /**
     * Attempts to handle an [APIGatewayProxyRequestEvent] by first looking for
     * a suitable [HandlerMethod] and then delegating the execution to it.
     */
    fun handle(request: APIGatewayProxyRequestEvent,
               httpMethodToHandlers: Map<HttpMethod, Set<HandlerMethod>>): APIGatewayProxyResponseEvent {

        return try {
            LOGGER.debug("Handling request to [{}]", request.path)

            handlerRequest(request, httpMethodToHandlers)
        } catch (e: RuntimeException) {
            alertDispatcher?.let {
                it.dispatchAlert(LambdaAlert("", e.message ?: e.localizedMessage))
            }

            LOGGER.error("Error handling request", e)

            APIGatewayProxyResponseEvent()
                    .withStatusCode(StatusCode.API_ERROR.code)
                    .withHeaders(mapOf("Content-Type" to "application/json"))
        }

    }

    private fun handlerRequest(request: APIGatewayProxyRequestEvent,
                               httpMethodToHandlers: Map<HttpMethod, Set<HandlerMethod>>
    ): APIGatewayProxyResponseEvent {

        val handlersForHttpMethod = httpMethodToHandlers[HttpMethod.valueOf(request.httpMethod)]
                ?: return APIGatewayProxyResponseEvent().withStatusCode(StatusCode.NOT_FOUND.code)

        val requestHandlerMethod = handlersForHttpMethod.asSequence()
                .sortedWith(HandlerMethodComparator())
                .find { it.matches(request.path, request.queryStringParameters) }
                ?: throw IllegalArgumentException(
                        "Handler not found for [${request.path}] and method [{${request.httpMethod}}]")

        LOGGER.debug("Handler method [{}] in [{}] selected",
                     requestHandlerMethod.httpMethod.name,
                     requestHandlerMethod.controllerClass.canonicalName)

        val response = endpointInvoker.invoke(requestHandlerMethod, request)

        return APIGatewayProxyResponseEvent().apply {
            withStatusCode(response.statusCode.code)
            withHeaders(response.headers + mapOf("Content-Type" to "application/json"))
            withBody(OBJECT_MAPPER.writeValueAsString(response.body))
        }
    }
}
