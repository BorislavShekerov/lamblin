/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.model

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.annotation.Header
import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import com.lamblin.core.model.annotation.RequestBody
import kotlin.reflect.KClass

internal const val REQUEST_BODY_MAPPED_NAME = "REQUEST_BODY"

/** Defines an endpoint method parameter. */
data class HandlerMethodParameter(
    /** The name of the target request parameter (i.e. query or path parameter name). */
    val annotationMappedName: String,

    /** The actual name of the parameter as defined in the endpoint argument list. */
    val name: String,

    /** Defines if the parameter is mandatory for the request to be processed. */
    val required: Boolean = false,

    /** The default value to use in absence of the param in the request. */
    val defaultValue: Any? = null,

    /** The type of the parameter. */
    val type: KClass<*>) {

    companion object {

        /** Creates a parameter which is neither a query nor a path param. */
        fun requestBodyParam(
            name: String,
            type: KClass<*>
        ): HandlerMethodParameter = HandlerMethodParameter(
            annotationMappedName = REQUEST_BODY_MAPPED_NAME,
            name = name,
            type = type)

        /** Creates a parameter targeting a path parameter. */
        fun pathParam(
            name: String,
            type: KClass<*>,
            param: PathParam
        ): HandlerMethodParameter = HandlerMethodParameter(
            annotationMappedName = param.value,
            name = name,
            type = type)

        /** Creates a parameter targeting a query parameter. */
        fun queryParam(
            name: String,
            type: KClass<*>,
            param: QueryParam
        ): HandlerMethodParameter {

            val defaultValue = if (param.defaultValue.isEmpty()) {
                null
            } else param.defaultValue

            return HandlerMethodParameter(
                annotationMappedName = param.value,
                required = param.required,
                name = name,
                type = type,
                defaultValue = defaultValue)
        }

        /** Creates a parameter targeting a header. */
        private fun headerParam(
            name: String,
            type: KClass<*>,
            param: Header
        ): HandlerMethodParameter = HandlerMethodParameter(
            annotationMappedName = param.value,
            name = name,
            type = type)

        private fun apiGatewayProxyRequestEventParam(name: String) = HandlerMethodParameter(
            annotationMappedName = name,
            name = name,
            type = APIGatewayProxyRequestEvent::class)

        fun of(
            name: String,
            type: KClass<*>,
            annotation: Annotation?
        ): HandlerMethodParameter =
            when (annotation) {
                is PathParam -> pathParam(name, type, annotation)
                is QueryParam -> queryParam(name, type, annotation)
                is Header -> headerParam(name, type, annotation)
                is RequestBody -> requestBodyParam(name, type)
                else -> {
                    if (type == APIGatewayProxyRequestEvent::class)  apiGatewayProxyRequestEventParam(name)
                    else throw IllegalArgumentException("Annotation $annotation not supported")
                }
            }
    }
}
