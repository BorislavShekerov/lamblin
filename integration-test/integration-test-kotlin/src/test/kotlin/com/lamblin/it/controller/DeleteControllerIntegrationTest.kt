package com.lamblin.it.controller

import com.lamblin.core.FrontController
import com.lamblin.core.model.HttpMethod
import com.lamblin.it.model.MULTIPLE_PATH_PARAM_DELETE_ENDPOINT
import com.lamblin.it.model.PATH_PARAM_1
import com.lamblin.it.model.PATH_PARAM_2
import com.lamblin.it.model.QUERY_PARAM_1
import com.lamblin.it.model.QUERY_PARAM_2
import com.lamblin.it.model.QUERY_PARAM_DELETE_ENDPOINT
import com.lamblin.it.model.SIMPLE_DELETE_ENDPOINT
import com.lamblin.it.model.SINGLE_PATH_PARAM_DELETE_ENDPOINT
import com.lamblin.it.model.createRequestInputStream
import com.lamblin.it.model.runRequestAndVerifyResponse
import org.junit.jupiter.api.Test

class DeleteControllerIntegrationTest {

    private val frontController = FrontController.instance(setOf(DeleteController()))

    @Test
    fun `should handle DELETE requests with no params`() {
        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(SIMPLE_DELETE_ENDPOINT, HttpMethod.DELETE),
                expectedResponseBodyContent = SIMPLE_DELETE_ENDPOINT)
    }

    @Test
    fun `should handle DELETE requests with single query param`() {
        val queryParamValue = "value"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_DELETE_ENDPOINT,
                        HttpMethod.DELETE,
                        mapOf(QUERY_PARAM_1 to queryParamValue)),
                expectedResponseBodyContent = "$QUERY_PARAM_DELETE_ENDPOINT-$queryParamValue")
    }

    @Test
    fun `should handle DELETE requests with multiple query params`() {
        val queryParam1Value = "value1"
        val queryParam2Value = "value2"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_DELETE_ENDPOINT,
                        HttpMethod.DELETE,
                        mapOf(
                                QUERY_PARAM_1 to queryParam1Value,
                                QUERY_PARAM_2 to queryParam2Value)),
                expectedResponseBodyContent = "$QUERY_PARAM_DELETE_ENDPOINT-$queryParam1Value,$queryParam2Value")
    }

    @Test
    fun `should handle DELETE requests with single path param`() {
        val pathParamValue = "value"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        SINGLE_PATH_PARAM_DELETE_ENDPOINT.replace("{$PATH_PARAM_1}", pathParamValue),
                        HttpMethod.DELETE),
                expectedResponseBodyContent = "$SINGLE_PATH_PARAM_DELETE_ENDPOINT-$pathParamValue")
    }

    @Test
    fun `should handle DELETE requests with multiple path params`() {
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_DELETE_ENDPOINT
                                .replace("{$PATH_PARAM_1}", pathParamValue1)
                                .replace("{$PATH_PARAM_2}", pathParamValue2),
                        HttpMethod.DELETE),
                expectedResponseBodyContent = "$MULTIPLE_PATH_PARAM_DELETE_ENDPOINT-$pathParamValue1,$pathParamValue2")
    }

    @Test
    fun `should handle DELETE requests with multiple path params and query params`() {
        val queryParamValue = "queryParamValue"
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_DELETE_ENDPOINT
                                .replace("{$PATH_PARAM_1}", pathParamValue1)
                                .replace("{$PATH_PARAM_2}", pathParamValue2),
                        HttpMethod.DELETE,
                        mapOf(QUERY_PARAM_1 to queryParamValue)),
                expectedResponseBodyContent = "$MULTIPLE_PATH_PARAM_DELETE_ENDPOINT-$queryParamValue,$pathParamValue1,$pathParamValue2")
    }

}