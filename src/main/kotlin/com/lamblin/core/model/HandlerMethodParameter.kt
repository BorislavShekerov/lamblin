package com.lamblin.core.model

import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import com.lamblin.core.model.annotation.RequestBody

/** Defines an endpoint method parameter. */
data class HandlerMethodParameter(
        /** The name of the target request parameter (i.e. query or path parameter name). */
        val annotationMappedName: String? = null,

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
        private fun simpleParam(
                name: String,
                type: Class<*>
        ): HandlerMethodParameter = HandlerMethodParameter(name = name, type = type)

        /** Creates a parameter targeting a path parameter. */
        private fun pathParam(
                name: String,
                type: Class<*>,
                param: PathParam
        ): HandlerMethodParameter = HandlerMethodParameter(
                annotationMappedName = param.value,
                name = name,
                type = type)

        /** Creates a parameter targeting a query parameter. */
        private fun queryParam(
                name: String,
                type: Class<*>,
                param: QueryParam
        ): HandlerMethodParameter = HandlerMethodParameter(
                annotationMappedName = param.value,
                name = name,
                type = type)

        fun of(
                name: String,
                type: Class<*>,
                annotation: Annotation
        ): HandlerMethodParameter = when (annotation) {

            is PathParam -> pathParam(name, type, annotation)
            is QueryParam -> queryParam(name, type, annotation)
            is RequestBody -> simpleParam(name, type)
            else -> throw IllegalArgumentException("Annotation $annotation not supported")
        }
    }
}
