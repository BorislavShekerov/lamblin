/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.handler

import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HandlerMethodParameter
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.security.AccessControl
import org.slf4j.LoggerFactory
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.valueParameters

/** Defines the mechanism for creating [HandlerMethod]. */
internal interface HandlerMethodFactory {
    fun method(method: KCallable<HttpResponse<*>>, controllerClass: KClass<out Any>): HandlerMethod

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
    override fun method(method: KCallable<HttpResponse<*>>, controller: KClass<out Any>): HandlerMethod {
        val endpointAnnotation = method.findAnnotation<Endpoint>()

        return endpointAnnotation?.method?.let {
            createHandlerMethod(it, endpointAnnotation.path, method, controller)
        } ?: throw IllegalStateException("Endpoint annotation not present on ${method.name}")
    }

    private fun createHandlerMethod(
        httpMethod: HttpMethod,
        path: String,
        method: KCallable<HttpResponse<*>>,
        controllerClass: KClass<out Any>
    ): HandlerMethod {

        val paramNameToParam = method.valueParameters.asSequence()
            .map { parameter ->
                parameter.name!! to HandlerMethodParameter.of(
                    parameter.name!!,
                    parameter.type.classifier as KClass<*>,
                    parameter.annotations.firstOrNull()) }
            .toMap()

        LOGGER.debug("Handler method created for [{}] in [{}]", controllerClass.simpleName, method.name)

        val accessControl = method.findAnnotation<AccessControl>()

        return HandlerMethod(
            path,
            httpMethod,
            paramNameToParam,
            method,
            controllerClass,
            accessControl)
    }
}
