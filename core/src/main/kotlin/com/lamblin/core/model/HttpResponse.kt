package com.lamblin.core.model

/** Defines the http response. */
data class HttpResponse<T>(
    val statusCode: StatusCode = StatusCode.OK,
    val headers: Map<String, String> = mapOf(),
    val body: T? = null) {

    companion object {
        @JvmStatic
        fun apiError(message: String) = HttpResponse(statusCode = StatusCode.API_ERROR, body = ApiError(message))

        @JvmStatic
        fun <T> ok(body: T?) = HttpResponse(body = body)

        @JvmStatic
        fun ok() = HttpResponse<Void>()

        @JvmStatic
        fun withCode(statusCode: StatusCode) = HttpResponse<Any>(statusCode = statusCode)
    }
}

data class ApiError(val message: String)
