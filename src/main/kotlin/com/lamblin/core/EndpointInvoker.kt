package com.lamblin.core

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.extract.EndpointParamValueInjector
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpResponse
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import java.lang.reflect.Parameter

private val LOGGER = LoggerFactory.getLogger(EndpointInvoker::class.java)

/**
 * Responsible for invoking a [HandlerMethod] using the details in a [APIGatewayProxyRequestEvent].
 */
internal class EndpointInvoker(
        private val endpointParamValueInjector: EndpointParamValueInjector,
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
                    ?: throw IllegalStateException("Controller not found for class [ ${controllerClass.canonicalName}]")

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
            method: Method,
            parameters: Array<Parameter>,
            controller: Any): HttpResponse<*> {

        try {
            return if (parameters.isEmpty())
                method.invoke(controller) as HttpResponse<*>
            else {
                val paramValues = endpointParamValueInjector
                        .injectParamValues(request,
                                           handlerMethod,
                                           handlerMethod.annotationMappedNameToParam()).values.toTypedArray()

                method.invoke(controller, *paramValues) as HttpResponse<*>
            }
        } catch (e: InvocationTargetException) {
            LOGGER.error("Exception occurred while executing handler.")
            throw e
        }
    }

}

fun HandlerMethod.annotationMappedNameToParam(): Map<String, Parameter> {
    val nameToParam = method.parameters
            .map { it.name to it }
            .toMap()

    return this.paramNameToParam.values
            .map {
                it.annotationMappedName to (nameToParam[it.name] ?: throw IllegalStateException(
                        "Param not found for name ${it.name}"))
            }.toMap()
}
