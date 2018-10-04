package it

import com.lamblin.core.FrontController
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.StatusCode
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import org.junit.jupiter.api.Test

const val CUSTOM_STATUS_CODE_ENDPOINT = "/get/custom-status-code"
const val SIMPLE_GET_PATH = "/get/simple"
const val QUERY_PARAM_PATH = "/get/query-param"
const val SINGLE_PATH_PARAM_PATH = "/get/path/{$PATH_PARAM_1}"
const val MULTIPLE_PATH_PARAM_PATH = "/get/path/{$PATH_PARAM_1}/foo/{$PATH_PARAM_2}"

class GetControllerIntegrationTest {

    private val frontController = FrontController.instance(setOf(GetController()))

    @Test
    fun `should handle GET requests with no params`() {
        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(SIMPLE_GET_PATH, HttpMethod.GET),
                expectedResponseBodyContent = SIMPLE_GET_PATH)
    }

    @Test
    fun `should handle GET requests with single query param`() {
        val queryParamValue = "value"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_PATH,
                        HttpMethod.GET,
                        mapOf(QUERY_PARAM_1 to queryParamValue)),
                expectedResponseBodyContent = "$QUERY_PARAM_PATH-$queryParamValue")
    }

    @Test
    fun `should handle GET requests with multiple query params`() {
        val queryParam1Value = "value1"
        val queryParam2Value = "value2"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_PATH,
                        HttpMethod.GET,
                        mapOf(
                                QUERY_PARAM_1 to queryParam1Value,
                                QUERY_PARAM_2 to queryParam2Value)),
                expectedResponseBodyContent = "$QUERY_PARAM_PATH-$queryParam1Value,$queryParam2Value")
    }

    @Test
    fun `should handle GET requests with single path param`() {
        val pathParamValue = "value"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        SINGLE_PATH_PARAM_PATH.replace("{$PATH_PARAM_1}", pathParamValue),
                        HttpMethod.GET),
                expectedResponseBodyContent = "$SINGLE_PATH_PARAM_PATH-$pathParamValue")
    }

    @Test
    fun `should handle GET requests with multiple path params`() {
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_PATH
                                .replace("{$PATH_PARAM_1}", pathParamValue1)
                                .replace("{$PATH_PARAM_2}", pathParamValue2),
                        HttpMethod.GET),
                expectedResponseBodyContent = "$MULTIPLE_PATH_PARAM_PATH-$pathParamValue1,$pathParamValue2")
    }

    @Test
    fun `should handle GET requests with multiple path params and query params`() {
        val queryParamValue = "queryParamValue"
        val pathParamValue1 = "value1"
        val pathParamValue2 = "value2"

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_PATH
                                .replace("{$PATH_PARAM_1}", pathParamValue1)
                                .replace("{$PATH_PARAM_2}", pathParamValue2),
                        HttpMethod.GET,
                        mapOf(QUERY_PARAM_1 to queryParamValue)),
                expectedResponseBodyContent = "$MULTIPLE_PATH_PARAM_PATH-$queryParamValue,$pathParamValue1,$pathParamValue2")
    }

    @Test
    fun `should return 404 for unknown routes`() {
        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream("/unknown", HttpMethod.GET),
                expectedStatusCode = 404)
    }

    @Test
    fun `should return status code returned from endpoint`() {
        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(CUSTOM_STATUS_CODE_ENDPOINT, HttpMethod.GET),
                expectedStatusCode = StatusCode.ACCEPTED.code)
    }

    class GetController {

        @Endpoint(SIMPLE_GET_PATH, method = HttpMethod.GET)
        fun simpleGetNoParams(): HttpResponse<ResponseEntity> {
            return HttpResponse.ok(ResponseEntity(SIMPLE_GET_PATH))
        }

        @Endpoint(QUERY_PARAM_PATH, method = HttpMethod.GET)
        fun signleQueryParamTest(@QueryParam(QUERY_PARAM_1) queryParam: String): HttpResponse<ResponseEntity> {
            return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_PATH-$queryParam"))
        }

        @Endpoint(QUERY_PARAM_PATH, method = HttpMethod.GET)
        fun multipleQueryParamTest(
                @QueryParam(QUERY_PARAM_1) queryParam1: String,
                @QueryParam(QUERY_PARAM_2) queryParam2: String): HttpResponse<ResponseEntity> {

            return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_PATH-$queryParam1,$queryParam2"))
        }

        @Endpoint(SINGLE_PATH_PARAM_PATH, method = HttpMethod.GET)
        fun singlePathParamPath(
                @PathParam(PATH_PARAM_1) pathParam: String): HttpResponse<ResponseEntity> {

            return HttpResponse.ok(ResponseEntity("$SINGLE_PATH_PARAM_PATH-$pathParam"))
        }

        @Endpoint(MULTIPLE_PATH_PARAM_PATH, method = HttpMethod.GET)
        fun multiplePathParamPath(
                @PathParam(PATH_PARAM_1) pathParamOne: String,
                @PathParam(PATH_PARAM_2) pathParamTwo: String): HttpResponse<ResponseEntity> {

            return HttpResponse.ok(ResponseEntity("$MULTIPLE_PATH_PARAM_PATH-$pathParamOne,$pathParamTwo"))
        }

        @Endpoint(MULTIPLE_PATH_PARAM_PATH, method = HttpMethod.GET)
        fun multiplePathParamWithQueryParamsPath(
                @QueryParam(QUERY_PARAM_1) queryParam: String,
                @PathParam(PATH_PARAM_1) pathParamOne: String,
                @PathParam(PATH_PARAM_2) pathParamTwo: String): HttpResponse<ResponseEntity> {

            return HttpResponse.ok(ResponseEntity("$MULTIPLE_PATH_PARAM_PATH-$queryParam,$pathParamOne,$pathParamTwo"))
        }

        @Endpoint(CUSTOM_STATUS_CODE_ENDPOINT, method = HttpMethod.GET)
        fun customStatusCodeEndpoint(): HttpResponse<ResponseEntity> {
            return HttpResponse(statusCode = StatusCode.ACCEPTED)
        }
    }
}
