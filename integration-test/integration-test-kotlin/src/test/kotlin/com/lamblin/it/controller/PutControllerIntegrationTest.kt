package com.lamblin.it.controller

import com.lamblin.it.controller.client.PutControllerClient
import com.lamblin.it.model.MULTI_PATH_PARAM_PUT_ENDPOINT
import com.lamblin.it.model.QUERY_PARAM_PUT_ENDPOINT
import com.lamblin.it.model.SIMPLE_PUT_ENDPOINT
import com.lamblin.it.model.SINGLE_PATH_PARAM_PUT_ENDPOINT
import com.lamblin.it.model.runRequestAndVerifyResponse
import com.lamblin.test.config.LamblinTestConfig
import com.lamblin.test.config.annotation.LamblinTestRunnerConfig
import com.lamblin.test.junit5.JUnit5LamblinExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JUnit5LamblinExtension::class)
@LamblinTestRunnerConfig(testConfigClass = PutControllerIntegrationTest.TestConfiguration::class)
class PutControllerIntegrationTest {

    @Test
    fun `should handle PUT requests with no params`() {
        runRequestAndVerifyResponse(
            PutControllerClient::callSimplePutNoParamsEndpoint,
            SIMPLE_PUT_ENDPOINT)
    }

    @Test
    fun `should handle PUT requests with single query param`() {
        val queryParamValue = "value"

        runRequestAndVerifyResponse(
            { PutControllerClient.callSingleQueryParamEndpoint(queryParamValue) },
            "$QUERY_PARAM_PUT_ENDPOINT-$queryParamValue")
    }

    @Test
    fun `should handle PUT requests with multiple query params`() {
        val queryParam1Value = "value1"
        val queryParam2Value = "value2"

        runRequestAndVerifyResponse(
            { PutControllerClient.callMultiQueryParamEndpoint(queryParam1Value, queryParam2Value) },
            "$QUERY_PARAM_PUT_ENDPOINT-$queryParam1Value,$queryParam2Value")
    }

    @Test
    fun `should handle PUT requests with single path param`() {
        val pathParamValue = "value"

        runRequestAndVerifyResponse(
            { PutControllerClient.callSinglePathParamEndpoint(pathParamValue) },
            "$SINGLE_PATH_PARAM_PUT_ENDPOINT-$pathParamValue")
    }

    @Test
    fun `should handle PUT requests with multiple path params`() {
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
            { PutControllerClient.callMultiPathParamEndpoint(pathParamValue1, pathParamValue2) },
            "$MULTI_PATH_PARAM_PUT_ENDPOINT-$pathParamValue1,$pathParamValue2")
    }

    @Test
    fun `should handle PUT requests with multiple path params and query params`() {
        val queryParamValue = "queryParamValue"
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
            {
                PutControllerClient.callMultiPathParamEndpointWithQueryParamEndpoint(
                    queryParamValue,
                    pathParamValue1,
                    pathParamValue2)
            },
            "$MULTI_PATH_PARAM_PUT_ENDPOINT-$queryParamValue,$pathParamValue1,$pathParamValue2")
    }

    class TestConfiguration : LamblinTestConfig {

        override fun controllers(): Set<Any> {
            return setOf(PutController())
        }
    }
}
