package it.kotlin

import com.lamblin.core.FrontController
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import com.lamblin.core.model.annotation.RequestBody
import it.ExampleRequestBody
import it.MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT
import it.PATH_PARAM_1
import it.PATH_PARAM_2
import it.QUERY_PARAM_1
import it.QUERY_PARAM_2
import it.QUERY_PARAM_PARAM_PUT_ENDPOINT
import it.ResponseEntity
import it.SIMPLE_PUT_ENDPOINT
import it.SIMPLE_REQUEST_BODY_PUT_ENDPOINT
import it.SINGLE_PATH_PARAM_PATH_PUT_ENDPOINT
import it.createRequestInputStream
import it.kotlin.controller.PutController
import it.runRequestAndVerifyResponse
import org.junit.jupiter.api.Test

class KotlinPutControllerIntegrationTest {

    private val frontController = FrontController.instance(setOf(PutController()))

    @Test
    fun `should handle PUT requests with no params`() {
        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(SIMPLE_PUT_ENDPOINT, HttpMethod.PUT),
                expectedResponseBodyContent = SIMPLE_PUT_ENDPOINT)
    }

    @Test
    fun `should handle PUT requests with single query param`() {
        val queryParamValue = "value"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_PARAM_PUT_ENDPOINT,
                        HttpMethod.PUT,
                        mapOf(QUERY_PARAM_1 to queryParamValue)),
                expectedResponseBodyContent = "$QUERY_PARAM_PARAM_PUT_ENDPOINT-$queryParamValue")
    }

    @Test
    fun `should handle PUT requests with multiple query params`() {
        val queryParam1Value = "value1"
        val queryParam2Value = "value2"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_PARAM_PUT_ENDPOINT,
                        HttpMethod.PUT,
                        mapOf(
                                QUERY_PARAM_1 to queryParam1Value,
                                QUERY_PARAM_2 to queryParam2Value)),
                expectedResponseBodyContent = "$QUERY_PARAM_PARAM_PUT_ENDPOINT-$queryParam1Value,$queryParam2Value")
    }

    @Test
    fun `should handle PUT requests with single path param`() {
        val pathParamValue = "value"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        SINGLE_PATH_PARAM_PATH_PUT_ENDPOINT.replace("{$PATH_PARAM_1}", pathParamValue),
                        HttpMethod.PUT),
                expectedResponseBodyContent = "$SINGLE_PATH_PARAM_PATH_PUT_ENDPOINT-$pathParamValue")
    }

    @Test
    fun `should handle PUT requests with multiple path params`() {
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT
                                .replace("{$PATH_PARAM_1}", pathParamValue1)
                                .replace("{$PATH_PARAM_2}", pathParamValue2),
                        HttpMethod.PUT),
                expectedResponseBodyContent = "$MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT-$pathParamValue1,$pathParamValue2")
    }

    @Test
    fun `should handle PUT requests with multiple path params and query params`() {
        val queryParamValue = "queryParamValue"
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT
                                .replace("{$PATH_PARAM_1}", pathParamValue1)
                                .replace("{$PATH_PARAM_2}", pathParamValue2),
                        HttpMethod.PUT,
                        mapOf(QUERY_PARAM_1 to queryParamValue)),
                expectedResponseBodyContent = "$MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT-$queryParamValue,$pathParamValue1,$pathParamValue2")
    }
}
