package it.kotlin

import com.lamblin.core.FrontController
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import com.lamblin.core.model.annotation.RequestBody
import it.ExampleRequestBody
import it.PATH_PARAM_1
import it.PATH_PARAM_2
import it.QUERY_PARAM_1
import it.QUERY_PARAM_2
import it.ResponseEntity
import it.createRequestInputStream
import it.kotlin.controller.PostController
import it.runRequestAndVerifyResponse
import org.junit.jupiter.api.Test

class KotlinPostControllerIntegrationTest {

    private val frontController = FrontController.instance(setOf(
            PostController()))

    @Test
    fun `should handle POST requests with no params`() {
        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(SIMPLE_POST_ENDPOINT, HttpMethod.POST),
                expectedResponseBodyContent = SIMPLE_POST_ENDPOINT)
    }

    @Test
    fun `should handle POST requests with single query param`() {
        val queryParamValue = "value"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_PARAM_POST_ENDPOINT,
                        HttpMethod.POST,
                        mapOf(QUERY_PARAM_1 to queryParamValue)),
                expectedResponseBodyContent = "$QUERY_PARAM_PARAM_POST_ENDPOINT-$queryParamValue")
    }

    @Test
    fun `should handle POST requests with multiple query params`() {
        val queryParam1Value = "value1"
        val queryParam2Value = "value2"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_PARAM_POST_ENDPOINT,
                        HttpMethod.POST,
                        mapOf(
                                QUERY_PARAM_1 to queryParam1Value,
                                QUERY_PARAM_2 to queryParam2Value)),
                expectedResponseBodyContent = "$QUERY_PARAM_PARAM_POST_ENDPOINT-$queryParam1Value,$queryParam2Value")
    }

    @Test
    fun `should handle POST requests with single path param`() {
        val pathParamValue = "value"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        SINGLE_PATH_PARAM_PATH_POST_ENDPOINT.replace("{$PATH_PARAM_1}", pathParamValue),
                        HttpMethod.POST),
                expectedResponseBodyContent = "$SINGLE_PATH_PARAM_PATH_POST_ENDPOINT-$pathParamValue")
    }

    @Test
    fun `should handle POST requests with multiple path params`() {
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_PATH_POST_ENDPOINT
                                .replace("{$PATH_PARAM_1}", pathParamValue1)
                                .replace("{$PATH_PARAM_2}", pathParamValue2),
                        HttpMethod.POST),
                expectedResponseBodyContent = "$MULTIPLE_PATH_PARAM_PATH_POST_ENDPOINT-$pathParamValue1,$pathParamValue2")
    }

    @Test
    fun `should handle POST requests with request body`() {
        val requestBody = ExampleRequestBody("test")

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        SIMPLE_REQUEST_BODY_POST_ENDPOINT,
                        HttpMethod.POST,
                        body = requestBody),
                expectedResponseBodyContent = "$SIMPLE_REQUEST_BODY_POST_ENDPOINT-${requestBody.body}")
    }

    @Test
    fun `should handle POST requests with multiple path params and query params`() {
        val queryParamValue = "queryParamValue"
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_PATH_POST_ENDPOINT
                                .replace("{$PATH_PARAM_1}", pathParamValue1)
                                .replace("{$PATH_PARAM_2}", pathParamValue2),
                        HttpMethod.POST,
                        mapOf(QUERY_PARAM_1 to queryParamValue)),
                expectedResponseBodyContent = "$MULTIPLE_PATH_PARAM_PATH_POST_ENDPOINT-$queryParamValue,$pathParamValue1,$pathParamValue2")
    }

}
