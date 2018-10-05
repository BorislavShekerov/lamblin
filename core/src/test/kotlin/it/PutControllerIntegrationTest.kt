package it

import com.lamblin.core.FrontController
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import com.lamblin.core.model.annotation.RequestBody
import org.junit.jupiter.api.Test

const val SIMPLE_PUT_ENDPOINT = "/put/simple"
const val SIMPLE_REQUEST_BODY_PUT_ENDPOINT = "/put/request-body"
const val QUERY_PARAM_PARAM_PUT_ENDPOINT = "/put/query-param"
const val SINGLE_PATH_PARAM_PATH_PUT_ENDPOINT = "/put/path/{$PATH_PARAM_1}"
const val MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT = "/put/path/{$PATH_PARAM_1}/foo/{$PATH_PARAM_2}"

class PutControllerIntegrationTest {

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

    class PutController {

        @Endpoint(SIMPLE_PUT_ENDPOINT, method = HttpMethod.PUT)
        fun simplePostNoParams(): HttpResponse<ResponseEntity> {
            return HttpResponse.ok(ResponseEntity(SIMPLE_PUT_ENDPOINT))
        }

        @Endpoint(QUERY_PARAM_PARAM_PUT_ENDPOINT, method = HttpMethod.PUT)
        fun singleQueryParamTest(@QueryParam(QUERY_PARAM_1) queryParam: String): HttpResponse<ResponseEntity> {
            return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_PARAM_PUT_ENDPOINT-$queryParam"))
        }

        @Endpoint(QUERY_PARAM_PARAM_PUT_ENDPOINT, method = HttpMethod.PUT)
        fun multipleQueryParamTest(
                @QueryParam(QUERY_PARAM_1) queryParam1: String,
                @QueryParam(QUERY_PARAM_2) queryParam2: String): HttpResponse<ResponseEntity> {

            return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_PARAM_PUT_ENDPOINT-$queryParam1,$queryParam2"))
        }

        @Endpoint(SINGLE_PATH_PARAM_PATH_PUT_ENDPOINT, method = HttpMethod.PUT)
        fun singlePathParamPath(
                @PathParam(PATH_PARAM_1) pathParam: String): HttpResponse<ResponseEntity> {

            return HttpResponse.ok(ResponseEntity("$SINGLE_PATH_PARAM_PATH_PUT_ENDPOINT-$pathParam"))
        }

        @Endpoint(SIMPLE_REQUEST_BODY_PUT_ENDPOINT, method = HttpMethod.PUT)
        fun requestBody(
                @RequestBody exampleRequestBody: PostControllerIntegrationTest.ExampleRequestBody): HttpResponse<ResponseEntity> {

            return HttpResponse.ok(ResponseEntity("$SIMPLE_REQUEST_BODY_PUT_ENDPOINT-${exampleRequestBody.body}"))
        }

        @Endpoint(MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT, method = HttpMethod.PUT)
        fun multiplePathParamPath(
                @PathParam(PATH_PARAM_1) pathParamOne: String,
                @PathParam(PATH_PARAM_2) pathParamTwo: String): HttpResponse<ResponseEntity> {

            return HttpResponse.ok(ResponseEntity("$MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT-$pathParamOne,$pathParamTwo"))
        }

        @Endpoint(MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT, method = HttpMethod.PUT)
        fun multiplePathParamWithQueryParamsPath(
                @QueryParam(QUERY_PARAM_1) queryParam: String,
                @PathParam(PATH_PARAM_1) pathParamOne: String,
                @PathParam(PATH_PARAM_2) pathParamTwo: String): HttpResponse<ResponseEntity> {

            return HttpResponse.ok(ResponseEntity("$MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT-$queryParam,$pathParamOne,$pathParamTwo"))
        }
    }
}
