package com.lamblin.it

import com.lamblin.core.Lamblin
import com.lamblin.core.model.StatusCode
import com.lamblin.it.controller.GetController
import com.lamblin.it.client.GetControllerClient
import com.lamblin.it.model.MULTI_PATH_PARAM_GET_ENDPOINT
import com.lamblin.it.model.QUERY_PARAM_GET_ENDPOINT
import com.lamblin.it.model.SIMPLE_GET_ENDPOINT
import com.lamblin.it.model.SINGLE_PATH_PARAM_GET_ENDPOINT
import com.lamblin.it.model.runRequestAndVerifyResponse
import com.lamblin.test.config.LamblinTestConfig
import com.lamblin.test.config.annotation.LamblinTestRunnerConfig
import com.lamblin.test.junit5.JUnit5LamblinExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JUnit5LamblinExtension::class)
@LamblinTestRunnerConfig(testConfigClass = GetControllerIntegrationTest.TestConfiguration::class)
class GetControllerIntegrationTest {

    @Test
    fun `should handle GET requests with no params`() {
        runRequestAndVerifyResponse(
            GetControllerClient::callSimpleGetNoParamsEndpoint,
            SIMPLE_GET_ENDPOINT)
    }

    @Test
    fun `should handle GET requests with single query param`() {
        val queryParamValue = "value"

        runRequestAndVerifyResponse(
            { GetControllerClient.callSingleQueryParamEndpoint(queryParamValue) },
            "$QUERY_PARAM_GET_ENDPOINT-$queryParamValue")
    }

    @Test
    fun `should handle GET requests with multiple query params`() {
        val queryParam1Value = "value1"
        val queryParam2Value = "value2"

        runRequestAndVerifyResponse(
            { GetControllerClient.callMultiQueryParamEndpoint(queryParam1Value, queryParam2Value) },
            "$QUERY_PARAM_GET_ENDPOINT-$queryParam1Value,$queryParam2Value")
    }

    @Test
    fun `should handle GET requests with single path param`() {
        val pathParamValue = "value"

        runRequestAndVerifyResponse(
            { GetControllerClient.callSinglePathParamEndpoint(pathParamValue) },
            "$SINGLE_PATH_PARAM_GET_ENDPOINT-$pathParamValue")
    }

    @Test
    fun `should handle GET requests with multiple path params`() {
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
            { GetControllerClient.callMultiPathParamEndpoint(pathParamValue1, pathParamValue2) },
            "$MULTI_PATH_PARAM_GET_ENDPOINT-$pathParamValue1,$pathParamValue2")
    }

    @Test
    fun `should handle GET requests with multiple path params and query params`() {
        val queryParamValue = "queryParamValue"
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
            {
                GetControllerClient.callMultiPathParamWithQueryParamEndpoint(
                    queryParamValue,
                    pathParamValue1,
                    pathParamValue2)
            },
            "$MULTI_PATH_PARAM_GET_ENDPOINT-$queryParamValue,$pathParamValue1,$pathParamValue2")
    }

    @Test
    fun `should return 404 for unknown routes`() {
        runRequestAndVerifyResponse(
            GetControllerClient::callUnknownEndpoint,
            expectedStatusCode = StatusCode.NOT_FOUND.code
        )
    }

    @Test
    fun `should return status code returned from endpoint`() {
        runRequestAndVerifyResponse(
            GetControllerClient::callCustomStatusCodeEndpoint,
            expectedStatusCode = StatusCode.ACCEPTED.code)
    }

    class TestConfiguration : LamblinTestConfig {

        override fun controllers(): Set<Any> {
            return setOf(GetController())
        }
    }
}
