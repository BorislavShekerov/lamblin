/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.model

import com.lamblin.core.security.AccessControl
import org.slf4j.LoggerFactory
import kotlin.reflect.KCallable
import kotlin.reflect.KClass

private val LOGGER = LoggerFactory.getLogger(HandlerMethod::class.java)

/** Defines the structure of an endpoint handling method. */
data class HandlerMethod(
    /** The URL path, i.e /foo/bar. */
        val path: String,

    /** The HTTP method handled. */
        val httpMethod: HttpMethod,

    /** Maps method parameter names to HandlerMethodParameter instances. */
        val paramNameToParam: Map<String, HandlerMethodParameter> = mapOf(),

    /** The reference to the actual method to be invoked. */
        val method: KCallable<HttpResponse<*>>,

    /** The reference to the method container class. */
        val controllerClass: KClass<out Any>,

    /** Defines the access control details for the handler. */
        val accessControl: AccessControl? = null) {

    companion object {

        /** Checks if a url path section has the capture format "{param}", i.e. surrounded by "{" and "}". */
        fun isPathParamSection(pathSection: String) = pathSection.startsWith("{")

    }

    /**
     * Tries to match the details in a request to the handler method.
     *
     * Returns true if the path of the request to be handled matches that of the handler method.
     * Path parameters are ignored in the check, meaning that any string is regarded as a valid request path parameter.
     *
     * @param requestPath the request path to check
     * @return true if request path matches the handler path
     */
    fun matches(requestPath: String, queryParams: Map<String, String>?): Boolean {
        LOGGER.debug("Matching path [{}] against request handler [{}]", requestPath, path)

        val requestPathEntries = requestPath.split("/")
        val expectedPathEntries = path.split("/")

        val requestPathMatchesEndpointPath = requestPathMatchesEndpointPath(requestPathEntries, expectedPathEntries)
        val requiredQueryParamsPresent = requiredQueryParamsPresent(queryParams)

        LOGGER.debug("Matching result: [{}]", requestPathMatchesEndpointPath && requiredQueryParamsPresent)

        return requestPathMatchesEndpointPath && requiredQueryParamsPresent
    }

    // Checks if the request path matches the endpoint handled path, ignoring path params
    private fun requestPathMatchesEndpointPath(
            requestPathEntries: List<String>,
            expectedPathEntries: List<String>
    ) =
            requestPathEntries.size == expectedPathEntries.size
                    && expectedPathEntries.asSequence().withIndex()
                    .filter { !isPathParamSection(it.value) }
                    .all { it.value == requestPathEntries[it.index] }

    // Checks if the required query params (without default values) are present
    private fun requiredQueryParamsPresent(queryParams: Map<String, String>?): Boolean {
        return paramNameToParam.values
                .filter { it.required && it.defaultValue == null }
                .all { queryParams?.containsKey(it.annotationMappedName) ?: false }
    }

}

enum class HttpMethod {
    POST, PUT, PATCH, GET, DELETE
}

