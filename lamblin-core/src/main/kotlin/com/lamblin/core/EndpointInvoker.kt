/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

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

            return invokeControllerMethod(
                MethodInvocationDetails(
                    handlerMethod,
                    request,
                    method,
                    parameters.toList(),
                    controller))
        } catch (e: IllegalAccessException) {
            LOGGER.error("Handler methods should have a public accessor modifier.", e)
            throw e
        } catch (e: InvocationTargetException) {
            LOGGER.error("Exception while executing method.", e)
            throw e
        }

    }

    private fun invokeControllerMethod(methodInvocationDetails: MethodInvocationDetails): HttpResponse<*> {
        return try {
            if (methodInvocationDetails.parameters.isEmpty())
                methodInvocationDetails.method.invoke(methodInvocationDetails.controller) as HttpResponse<*>
            else {
                val paramValues = endpointParamValueInjector
                    .injectParamValues(
                        methodInvocationDetails.request,
                        methodInvocationDetails.handlerMethod,
                        methodInvocationDetails.handlerMethod.annotationMappedNameToParam())
                    .values.toTypedArray()

                methodInvocationDetails.method.invoke(methodInvocationDetails.controller, *paramValues)
                        as HttpResponse<*>
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
            it.annotationMappedName to (nameToParam[it.name]
                ?: throw IllegalStateException("Param not found for name ${it.name}"))
        }.toMap()
}

private data class MethodInvocationDetails(
    val handlerMethod: HandlerMethod,
    val request: APIGatewayProxyRequestEvent,
    val method: Method,
    val parameters: List<Parameter>,
    val controller: Any)
