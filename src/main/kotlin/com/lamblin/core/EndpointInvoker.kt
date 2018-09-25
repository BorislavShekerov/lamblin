package com.lamblin.core

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpResponse
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KCallable
import kotlin.reflect.KParameter

private val LOGGER = LoggerFactory.getLogger(EndpointInvoker::class.java)

/**
 * Responsible for invoking a [HandlerMethod] using the details in a [APIGatewayProxyRequestEvent].
 */
internal class EndpointInvoker(
        private val requestToParamValueMapper: RequestToParamValueMapper,
        private val controllerRegistry: ControllerRegistry) {

    /**
     * Invokes the handler method, using the details of the request.
     *
     * @param handlerMethod the method to invoke
     * @param request the request
     * @return the method invokation response
     */
    fun invoke(handlerMethod: HandlerMethod, request: APIGatewayProxyRequestEvent): HttpResponse<*> {
        val method = handlerMethod.method
        val parameters = method.parameters
        val controllerClass = handlerMethod.controllerClass

        try {
            val controller = controllerRegistry.controllerForClass(controllerClass)
                    ?: IllegalStateException("Controller not found for class [ ${controllerClass.qualifiedName}]")

            return invokeControllerMethod(handlerMethod, request, method, parameters, controller)
        } catch (e: IllegalAccessException) {
            LOGGER.error("Handler methods should have a public accessor modifier.", e)
            throw e
        } catch (e: InvocationTargetException) {
            LOGGER.error("Exception while executing method.", e)
            throw e
        }

    }

    @Throws(IllegalAccessException::class, InvocationTargetException::class)
    private fun invokeControllerMethod(
            handlerMethod: HandlerMethod,
            request: APIGatewayProxyRequestEvent,
            method: KCallable<*>,
            parameters: List<KParameter>,
            controller: Any): HttpResponse<*> {

        try {
            return (if (parameters.isEmpty())
                method.call()
            else
                method.call(requestToParamValueMapper
                                    .mapRequestParamsToValues(request, handlerMethod))) as HttpResponse<*>
        } catch (e: InvocationTargetException) {
            LOGGER.error("Exception occurred while executing handler.")
            throw e
        }
    }

}
