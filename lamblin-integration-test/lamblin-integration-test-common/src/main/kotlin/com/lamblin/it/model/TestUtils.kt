/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.it.model

import com.lamblin.core.model.StatusCode
import org.assertj.core.api.Assertions.assertThat
import retrofit2.Response

const val QUERY_PARAM_1 = "query_1"
const val QUERY_PARAM_2 = "query_2"

const val DEFAULT_QUERY_PARAM_VALUE = "default-value"

const val PATH_PARAM_1 = "path_param_1"
const val PATH_PARAM_2 = "path_param_2"

const val AUTHORIZATION_HEADER = "Authorization"
const val AUTHORIZATION_HEADER_VALUE = "Bearer xxx"

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
