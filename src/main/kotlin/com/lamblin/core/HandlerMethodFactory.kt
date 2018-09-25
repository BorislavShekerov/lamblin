package com.lamblin.core

import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HandlerMethodParameter
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpMethod.GET
import com.lamblin.core.model.HttpMethod.PATCH
import com.lamblin.core.model.HttpMethod.POST
import com.lamblin.core.model.HttpMethod.PUT
import com.lamblin.core.model.annotation.Endpoint
import org.slf4j.LoggerFactory
import kotlin.reflect.KCallable
import kotlin.reflect.KClass

private val LOGGER = LoggerFactory.getLogger(HandlerMethodFactory::class.java)

internal interface HandlerMethodFactory {
    fun method(method: KCallable<Any?>, controllerClass: KClass<out Any>): HandlerMethod

    companion object {
        internal fun default() = DefaultHandlerMethodFactory
    }
}
/**
 * Defines the mechanism for creating [HandlerMethod] instances after inspecting
 * the details(annotations) of a [KCallable].
 */
internal object DefaultHandlerMethodFactory: HandlerMethodFactory {

    /** Creates a [HandlerMethod] instance using the details(annotations and parameters) of the input [KCallable]. */
    override fun method(method: KCallable<Any?>, controllerClass: KClass<out Any>): HandlerMethod {
        val endpointAnnotation = method.annotations.find { it is Endpoint } as? Endpoint

        return when (endpointAnnotation?.method) {
            POST -> createHandlerMethod(POST, endpointAnnotation.path, method, controllerClass)
            GET -> createHandlerMethod(GET, endpointAnnotation.path, method, controllerClass)
            PUT -> createHandlerMethod(PUT, endpointAnnotation.path, method, controllerClass)
            PATCH -> createHandlerMethod(PATCH, endpointAnnotation.path, method, controllerClass)
            else -> throw IllegalArgumentException("Http Method ${endpointAnnotation?.method} not supported.")
        }
    }

    private fun createHandlerMethod(
            requestMethod: HttpMethod,
            path: String,
            method: KCallable<Any?>,
            controllerClass: KClass<out Any>): HandlerMethod {

        val paramNameToParam = method.parameters.asSequence()
                .filter { !it.annotations.isEmpty() }
                .map { it.name!! to HandlerMethodParameter.of(it.name!!, it.javaClass , it.annotations[0]) }
                .toMap()

        LOGGER.debug("Handler method created for [{}] in [{}]", controllerClass.qualifiedName, method.name)

        return HandlerMethod(path, requestMethod, paramNameToParam, method, controllerClass)
    }
}
