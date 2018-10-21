/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.handler

import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HandlerMethodParameter
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpMethod.DELETE
import com.lamblin.core.model.HttpMethod.GET
import com.lamblin.core.model.HttpMethod.PATCH
import com.lamblin.core.model.HttpMethod.POST
import com.lamblin.core.model.HttpMethod.PUT
import com.lamblin.core.model.annotation.Endpoint
import org.slf4j.LoggerFactory
import java.lang.IllegalStateException
import java.lang.reflect.Method
import kotlin.reflect.KCallable

/** Defines the mechanism for creating [HandlerMethod]. */
internal interface HandlerMethodFactory {
    fun method(method: Method, controllerClass: Class<out Any>): HandlerMethod

    companion object {
        internal fun default() = DefaultHandlerMethodFactory
    }
}

private val LOGGER = LoggerFactory.getLogger(DefaultHandlerMethodFactory::class.java)

/**
 * Defines the mechanism for creating [HandlerMethod] instances after inspecting
 * the details(annotations) of a [KCallable].
 */
internal object DefaultHandlerMethodFactory : HandlerMethodFactory {

    /** Creates a [HandlerMethod] frontController using the details(annotations and parameters) of the [KCallable]. */
    override fun method(method: Method, controller: Class<out Any>): HandlerMethod {
        val endpointAnnotation = method.annotations.find { it is Endpoint } as? Endpoint

        return endpointAnnotation?.method?.let {
            createHandlerMethod(it, endpointAnnotation.path, method, controller)
        } ?: throw IllegalStateException("Endpoint annotation not present on ${method.name}")
    }

    private fun createHandlerMethod(
        httpMethod: HttpMethod,
        path: String,
        method: Method,
        controllerClass: Class<out Any>
    ): HandlerMethod {

        val paramNameToParam = method.parameters.asSequence()
            .map { parameter ->
                parameter.name!! to HandlerMethodParameter.of(
                    parameter.name!!,
                    parameter.type,
                    parameter.annotations.firstOrNull()) }
            .toMap()

        LOGGER.debug("Handler method created for [{}] in [{}]", controllerClass.canonicalName, method.name)

        return HandlerMethod(path, httpMethod, paramNameToParam, method, controllerClass)
    }
}
