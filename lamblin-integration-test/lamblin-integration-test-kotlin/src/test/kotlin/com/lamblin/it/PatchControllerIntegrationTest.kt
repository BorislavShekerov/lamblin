package com.lamblin.it

import com.lamblin.it.controller.PatchController
import com.lamblin.it.client.PatchControllerClient
import com.lamblin.it.model.ExampleRequestBody
import com.lamblin.it.model.MULTI_PATH_PARAM_PATCH_ENDPOINT
import com.lamblin.it.model.QUERY_PARAM_PATCH_ENDPOINT
import com.lamblin.it.model.SIMPLE_PATCH_ENDPOINT
import com.lamblin.it.model.SIMPLE_REQUEST_BODY_PATCH_ENDPOINT
import com.lamblin.it.model.SINGLE_PATH_PARAM_PATCH_ENDPOINT
import com.lamblin.it.model.runRequestAndVerifyResponse
import com.lamblin.test.config.LamblinTestConfig
import com.lamblin.test.config.annotation.LamblinTestRunnerConfig
import com.lamblin.test.junit5.JUnit5LamblinExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JUnit5LamblinExtension::class)
@LamblinTestRunnerConfig(testConfigClass = PatchControllerIntegrationTest.TestConfiguration::class)
class PatchControllerIntegrationTest {

    @Test
    fun `should handle PATCH requests with no params`() {
        runRequestAndVerifyResponse(
            PatchControllerClient::callSimplePatchNoParamsEndpoint,
            SIMPLE_PATCH_ENDPOINT)
    }

    @Test
    fun `should handle PATCH requests with single query param`() {
        val queryParamValue = "value"

        runRequestAndVerifyResponse(
            { PatchControllerClient.callSingleQueryParamEndpoint(queryParamValue) },
            "$QUERY_PARAM_PATCH_ENDPOINT-$queryParamValue")
    }

    @Test
    fun `should handle PATCH requests with multiple query params`() {
        val queryParam1Value = "value1"
        val queryParam2Value = "value2"

        runRequestAndVerifyResponse(
            { PatchControllerClient.callMultiQueryParamEndpoint(queryParam1Value, queryParam2Value) },
            "$QUERY_PARAM_PATCH_ENDPOINT-$queryParam1Value,$queryParam2Value")
    }

    @Test
    fun `should handle PATCH requests with single path param`() {
        val pathParamValue = "value"

        runRequestAndVerifyResponse(
            { PatchControllerClient.callSinglePathParamEndpoint(pathParamValue) },
            "$SINGLE_PATH_PARAM_PATCH_ENDPOINT-$pathParamValue")
    }

    @Test
    fun `should handle PATCH requests with multiple path params`() {
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
            { PatchControllerClient.callMultiPathParamEndpoint(pathParamValue1, pathParamValue2) },
            "$MULTI_PATH_PARAM_PATCH_ENDPOINT-$pathParamValue1,$pathParamValue2")
    }

    @Test
    fun `should handle PATCH requests with request body`() {
        val requestBody = ExampleRequestBody("test")

        runRequestAndVerifyResponse(
            { PatchControllerClient.callRequestBodyEndpoint(requestBody) },
            "$SIMPLE_REQUEST_BODY_PATCH_ENDPOINT-${requestBody.body}")
    }

    @Test
    fun `should handle PATCH requests with multiple path params and query params`() {
        val queryParamValue = "queryParamValue"
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
            {
                PatchControllerClient.callMultiPathParamEndpointWithQueryParam(
                    queryParamValue,
                    pathParamValue1,
                    pathParamValue2)
            },
            "$MULTI_PATH_PARAM_PATCH_ENDPOINT-$queryParamValue,$pathParamValue1,$pathParamValue2")
    }

    class TestConfiguration : LamblinTestConfig {

        override fun controllers(): Set<Any> {
            return setOf(PatchController())
        }
    }
}
