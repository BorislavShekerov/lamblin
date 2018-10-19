/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.model

import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import com.lamblin.core.model.annotation.RequestBody

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
    val type: Class<*>) {

    companion object {

        /** Creates a parameter which is neither a query nor a path param. */
        fun requestBodyParam(
            name: String,
            type: Class<*>
        ): HandlerMethodParameter = HandlerMethodParameter(
            annotationMappedName = REQUEST_BODY_MAPPED_NAME,
            name = name,
            type = type)

        /** Creates a parameter targeting a path parameter. */
        fun pathParam(
            name: String,
            type: Class<*>,
            param: PathParam
        ): HandlerMethodParameter = HandlerMethodParameter(
            annotationMappedName = param.value,
            name = name,
            type = type)

        /** Creates a parameter targeting a query parameter. */
        fun queryParam(
            name: String,
            type: Class<*>,
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

        fun of(
            name: String,
            type: Class<*>,
            annotation: Annotation
        ): HandlerMethodParameter =
            when (annotation) {
                is PathParam -> pathParam(name, type, annotation)
                is QueryParam -> queryParam(name, type, annotation)
                is RequestBody -> requestBodyParam(name, type)
                else -> throw IllegalArgumentException("Annotation $annotation not supported")
            }
    }
}
