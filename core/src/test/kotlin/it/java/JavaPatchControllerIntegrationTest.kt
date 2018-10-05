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
import it.java.controller.PatchController
import it.runRequestAndVerifyResponse
import org.junit.jupiter.api.Test

const val SIMPLE_PATCH_ENDPOINT = "/patch/simple"
const val SIMPLE_REQUEST_BODY_PATCH_ENDPOINT = "/patch/request-body"
const val QUERY_PARAM_PARAM_PATCH_ENDPOINT = "/patch/query-param"
const val SINGLE_PATH_PARAM_PATH_PATCH_ENDPOINT = "/patch/path/{$PATH_PARAM_1}"
const val MULTIPLE_PATH_PARAM_PATH_PATCH_ENDPOINT = "/patch/path/{$PATH_PARAM_1}/foo/{$PATH_PARAM_2}"

class JavaPatchControllerIntegrationTest {

    private val frontController = FrontController.instance(setOf(
            PatchController()))

    @Test
    fun `should handle PATCH requests with no params`() {
        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(SIMPLE_PATCH_ENDPOINT, HttpMethod.PATCH),
                expectedResponseBodyContent = SIMPLE_PATCH_ENDPOINT)
    }

    @Test
    fun `should handle PATCH requests with single query param`() {
        val queryParamValue = "value"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_PARAM_PATCH_ENDPOINT,
                        HttpMethod.PATCH,
                        mapOf(QUERY_PARAM_1 to queryParamValue)),
                expectedResponseBodyContent = "$QUERY_PARAM_PARAM_PATCH_ENDPOINT-$queryParamValue")
    }

    @Test
    fun `should handle PATCH requests with multiple query params`() {
        val queryParam1Value = "value1"
        val queryParam2Value = "value2"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_PARAM_PATCH_ENDPOINT,
                        HttpMethod.PATCH,
                        mapOf(
                                QUERY_PARAM_1 to queryParam1Value,
                                QUERY_PARAM_2 to queryParam2Value)),
                expectedResponseBodyContent = "$QUERY_PARAM_PARAM_PATCH_ENDPOINT-$queryParam1Value,$queryParam2Value")
    }

    @Test
    fun `should handle PATCH requests with single path param`() {
        val pathParamValue = "value"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        SINGLE_PATH_PARAM_PATH_PATCH_ENDPOINT.replace("{$PATH_PARAM_1}", pathParamValue),
                        HttpMethod.PATCH),
                expectedResponseBodyContent = "$SINGLE_PATH_PARAM_PATH_PATCH_ENDPOINT-$pathParamValue")
    }

    @Test
    fun `should handle PATCH requests with multiple path params`() {
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_PATH_PATCH_ENDPOINT
                                .replace("{$PATH_PARAM_1}", pathParamValue1)
                                .replace("{$PATH_PARAM_2}", pathParamValue2),
                        HttpMethod.PATCH),
                expectedResponseBodyContent = "$MULTIPLE_PATH_PARAM_PATH_PATCH_ENDPOINT-$pathParamValue1,$pathParamValue2")
    }

    @Test
    fun `should handle PATCH requests with multiple path params and query params`() {
        val queryParamValue = "queryParamValue"
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_PATH_PATCH_ENDPOINT
                                .replace("{$PATH_PARAM_1}", pathParamValue1)
                                .replace("{$PATH_PARAM_2}", pathParamValue2),
                        HttpMethod.PATCH,
                        mapOf(QUERY_PARAM_1 to queryParamValue)),
                expectedResponseBodyContent = "$MULTIPLE_PATH_PARAM_PATH_PATCH_ENDPOINT-$queryParamValue,$pathParamValue1,$pathParamValue2")
    }

    @Test
    fun `should handle PATCH requests with request body`() {
        val requestBody = ExampleRequestBody("test")

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        SIMPLE_REQUEST_BODY_PATCH_ENDPOINT,
                        HttpMethod.PATCH,
                        body = requestBody),
                expectedResponseBodyContent = "$SIMPLE_REQUEST_BODY_PATCH_ENDPOINT-${requestBody.body}")
    }

}
