package com.lamblin.it

import com.lamblin.it.controller.PostController
import com.lamblin.it.client.PostControllerClient
import com.lamblin.it.model.ExampleRequestBody
import com.lamblin.it.model.MULTI_PATH_PARAM_POST_ENDPOINT
import com.lamblin.it.model.QUERY_PARAM_POST_ENDPOINT
import com.lamblin.it.model.SIMPLE_POST_ENDPOINT
import com.lamblin.it.model.SIMPLE_REQUEST_BODY_POST_ENDPOINT
import com.lamblin.it.model.SINGLE_PATH_PARAM_POST_ENDPOINT
import com.lamblin.it.model.runRequestAndVerifyResponse
import com.lamblin.test.config.LamblinTestConfig
import com.lamblin.test.config.annotation.LamblinTestRunnerConfig
import com.lamblin.test.junit5.JUnit5LamblinExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JUnit5LamblinExtension::class)
@LamblinTestRunnerConfig(testConfigClass = PostControllerIntegrationTest.TestConfiguration::class)
class PostControllerIntegrationTest {

    @Test
    fun `should handle POST requests with no params`() {
        runRequestAndVerifyResponse(
            PostControllerClient::callSimplePostNoParamsEndpoint,
            SIMPLE_POST_ENDPOINT)
    }

    @Test
    fun `should handle POST requests with single query param`() {
        val queryParamValue = "value"

        runRequestAndVerifyResponse(
            { PostControllerClient.callSingleQueryParamEndpoint(queryParamValue) },
            "$QUERY_PARAM_POST_ENDPOINT-$queryParamValue")
    }

    @Test
    fun `should handle POST requests with multiple query params`() {
        val queryParam1Value = "value1"
        val queryParam2Value = "value2"

        runRequestAndVerifyResponse(
            { PostControllerClient.callMultiQueryParamEndpoint(queryParam1Value, queryParam2Value) },
            "$QUERY_PARAM_POST_ENDPOINT-$queryParam1Value,$queryParam2Value")
    }

    @Test
    fun `should handle POST requests with single path param`() {
        val pathParamValue = "value"

        runRequestAndVerifyResponse(
            { PostControllerClient.callSinglePathParamEndpoint(pathParamValue) },
            "$SINGLE_PATH_PARAM_POST_ENDPOINT-$pathParamValue")
    }

    @Test
    fun `should handle POST requests with multiple path params`() {
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
            { PostControllerClient.callMultiPathParamEndpoint(pathParamValue1, pathParamValue2) },
            "$MULTI_PATH_PARAM_POST_ENDPOINT-$pathParamValue1,$pathParamValue2")
    }

    @Test
    fun `should handle POST requests with request body`() {
        val requestBody = ExampleRequestBody("test")

        runRequestAndVerifyResponse(
            { PostControllerClient.callRequestBodyEndpoint(requestBody) },
            "$SIMPLE_REQUEST_BODY_POST_ENDPOINT-${requestBody.body}")
    }

    @Test
    fun `should handle POST requests with multiple path params and query params`() {
        val queryParamValue = "queryParamValue"
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
            {
                PostControllerClient.callMultiPathParamEndpointWithQueryParam(
                    queryParamValue,
                    pathParamValue1,
                    pathParamValue2)
            },
            "$MULTI_PATH_PARAM_POST_ENDPOINT-$queryParamValue,$pathParamValue1,$pathParamValue2")
    }

    class TestConfiguration : LamblinTestConfig {

        override fun controllers(): Set<Any> {
            return setOf(PostController())
        }
    }

}
