package com.lamblin.it.controller

import com.lamblin.it.controller.client.DeleteControllerClient
import com.lamblin.it.model.MULTIPLE_PATH_PARAM_DELETE_ENDPOINT
import com.lamblin.it.model.QUERY_PARAM_DELETE_ENDPOINT
import com.lamblin.it.model.SIMPLE_DELETE_ENDPOINT
import com.lamblin.it.model.SINGLE_PATH_PARAM_DELETE_ENDPOINT
import com.lamblin.it.model.runRequestAndVerifyResponse
import com.lamblin.test.config.LamblinTestConfig
import com.lamblin.test.config.annotation.LamblinTestRunnerConfig
import com.lamblin.test.junit5.JUnit5LamblinExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JUnit5LamblinExtension::class)
@LamblinTestRunnerConfig(testConfigClass = DeleteControllerIntegrationTest.TestConfiguration::class)
class DeleteControllerIntegrationTest {

    @Test
    fun `should handle DELETE requests with no params`() {
        runRequestAndVerifyResponse(
            DeleteControllerClient::callSimpleDeleteNoParamsEndpoint,
            SIMPLE_DELETE_ENDPOINT)
    }

    @Test
    fun `should handle DELETE requests with single query param`() {
        val queryParamValue = "value"

        runRequestAndVerifyResponse(
            { DeleteControllerClient.callSingleQueryParamEndpoint(queryParamValue) },
            "$QUERY_PARAM_DELETE_ENDPOINT-$queryParamValue")
    }

    @Test
    fun `should handle DELETE requests with multiple query params`() {
        val queryParam1Value = "value1"
        val queryParam2Value = "value2"

        runRequestAndVerifyResponse(
            { DeleteControllerClient.callMultiQueryParamEndpoint(queryParam1Value, queryParam2Value) },
            "$QUERY_PARAM_DELETE_ENDPOINT-$queryParam1Value,$queryParam2Value")
    }

    @Test
    fun `should handle DELETE requests with single path param`() {
        val pathParamValue = "value"

        runRequestAndVerifyResponse(
            { DeleteControllerClient.callSinglePathParamEndpoint(pathParamValue) },
            "$SINGLE_PATH_PARAM_DELETE_ENDPOINT-$pathParamValue")
    }

    @Test
    fun `should handle DELETE requests with multiple path params`() {
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
            { DeleteControllerClient.callMultiPathParamEndpoint(pathParamValue1, pathParamValue2) },
            "$MULTIPLE_PATH_PARAM_DELETE_ENDPOINT-$pathParamValue1,$pathParamValue2")
    }

    @Test
    fun `should handle DELETE requests with multiple path params and query params`() {
        val queryParamValue = "queryParamValue"
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
            {
                DeleteControllerClient.callMultiPathParamWithQueryParamEndpoint(
                    queryParamValue,
                    pathParamValue1,
                    pathParamValue2)
            },
            "$MULTIPLE_PATH_PARAM_DELETE_ENDPOINT-$queryParamValue,$pathParamValue1,$pathParamValue2")
    }

    class TestConfiguration : LamblinTestConfig {

        override fun controllers(): Set<Any> {
            return setOf(DeleteController())
        }
    }

}