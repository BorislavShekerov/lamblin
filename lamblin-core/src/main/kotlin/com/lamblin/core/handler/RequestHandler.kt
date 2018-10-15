/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.handler

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.lamblin.core.ControllerRegistry
import com.lamblin.core.EndpointInvoker
import com.lamblin.core.OBJECT_MAPPER
import com.lamblin.core.extract.EndpointParamValueInjectorComposite
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HandlerMethodComparator
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.StatusCode
import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger(RequestHandler::class.java)

internal class RequestHandler(private val endpointInvoker: EndpointInvoker) {

    companion object {
        internal fun instance(controllerRegistry: ControllerRegistry) = RequestHandler(
            EndpointInvoker(
                EndpointParamValueInjectorComposite.instance(),
                controllerRegistry))
    }

    /**
     * Attempts to handle an [APIGatewayProxyRequestEvent] by first looking for
     * a suitable [HandlerMethod] and then delegating the execution to it.
     */
    fun handle(
        request: APIGatewayProxyRequestEvent,
        httpMethodToHandlers: Map<HttpMethod, Set<HandlerMethod>>
    ): APIGatewayProxyResponseEvent {

        return try {
            LOGGER.debug("Handling request to [{}]", request.path)

            handlerRequest(request, httpMethodToHandlers)
        } catch (e: RuntimeException) {
            LOGGER.error("Error handling request", e)

            APIGatewayProxyResponseEvent()
                .withStatusCode(StatusCode.API_ERROR.code)
                .withHeaders(mapOf("Content-Type" to "application/json"))
        }

    }

    private fun handlerRequest(
        request: APIGatewayProxyRequestEvent,
        httpMethodToHandlers: Map<HttpMethod, Set<HandlerMethod>>
    ): APIGatewayProxyResponseEvent {

        val handlersForHttpMethod = httpMethodToHandlers[HttpMethod.valueOf(request.httpMethod)]
                ?: return APIGatewayProxyResponseEvent().withStatusCode(StatusCode.NOT_FOUND.code)

        val requestHandlerMethod = handlersForHttpMethod.asSequence()
            .sortedWith(HandlerMethodComparator())
            .find { it.matches(request.path, request.queryStringParameters) }
                ?: return APIGatewayProxyResponseEvent().withStatusCode(StatusCode.NOT_FOUND.code)

        LOGGER.debug(
            "Handler method [{}] in [{}] selected",
            requestHandlerMethod.httpMethod.name,
            requestHandlerMethod.controllerClass.canonicalName)

        val response = endpointInvoker.invoke(requestHandlerMethod, request)

        return createApiGatewayResponseEvent(response)
    }

    private fun createApiGatewayResponseEvent(
        response: HttpResponse<*>
    ): APIGatewayProxyResponseEvent =
        if (response.statusCode === StatusCode.OK) {
            APIGatewayProxyResponseEvent().apply {
                withStatusCode(response.statusCode.code)
                withHeaders(response.headers + mapOf("Content-Type" to "application/json"))

                response.body?.let { withBody(OBJECT_MAPPER.writeValueAsString(response.body)) }
            }
        } else {
            APIGatewayProxyResponseEvent().apply {
                withStatusCode(response.statusCode.code)
                withHeaders(response.headers)
            }
        }
}
