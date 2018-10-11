package com.lamblin.it.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.lamblin.core.model.StatusCode
import org.assertj.core.api.Assertions.assertThat
import retrofit2.Response

val OBJECT_MAPPER = ObjectMapper().apply {
    registerModule(JavaTimeModule())
    registerModule(KotlinModule())
}

const val QUERY_PARAM_1 = "query_1"
const val QUERY_PARAM_2 = "query_2"

const val PATH_PARAM_1 = "path_param_1"
const val PATH_PARAM_2 = "path_param_2"

const val DEFAULT_LOCALHOST_URL = "http://localhost:8080"

@JvmOverloads
fun <T> runRequestAndVerifyResponse(
    requestDispatcherFn: () -> Response<T>,
    expectedResponseBodyContent: String? = null,
    expectedStatusCode: Int = StatusCode.OK.code
) {

    val response = requestDispatcherFn()

    assertThat(response.code()).isEqualTo(expectedStatusCode)
    expectedResponseBodyContent?.let {
        assertThat(response.body()).isEqualTo(ResponseEntity(it))
    }
}

fun getServerBaseUrl() = System.getenv("API_BASE_URL") ?: DEFAULT_LOCALHOST_URL
