/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.extract.QueryParamValueInjector
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.QueryParam
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val DEFAULT_PARAM_VALUE = "default"
private const val DEFAULT_NON_STRING_PARAM_VALUE = "10"

class QueryParamValueInjectorTest {

    @Test
    fun `should return the value for each query param`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)
        every { request.multiValueQueryStringParameters } returns null
        every { request.queryStringParameters } returns mapOf("query1" to "value1")

        val queryParameter = TestController::class.members
            .find { it.name == "endpoint" }!!
            .parameters.find { it.name == "queryParam" }!!

        val result = QueryParamValueInjector.injectParamValues(
            request,
            mockk(),
            mapOf("query1" to queryParameter))

        assertThat(result["query1"]).isEqualTo("value1")
    }

    @Test
    fun `should return empty map if no query string params and default value not defined in @QueryParam`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)
        every { request.multiValueQueryStringParameters } returns null
        every { request.queryStringParameters } returns null

        val queryParameter = TestController::class.members
            .find { it.name == "endpoint" }!!
            .parameters.find { it.name == "queryParam" }!!

        val result = QueryParamValueInjector.injectParamValues(
            request,
            mockk(),
            mapOf("query1" to queryParameter))

        assertThat(result).isEqualTo(mapOf("query1" to null))
    }

    @Test
    fun `should return default value if query param not present and default value defined in @QueryParam`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)
        every { request.multiValueQueryStringParameters } returns null
        every { request.queryStringParameters } returns null

        val queryParameter = TestController::class.members
            .find { it.name == "endpointWithDefaultQueryParam" }!!
            .parameters.find { it.name == "queryParam" }!!

        val result = QueryParamValueInjector.injectParamValues(
            request,
            mockk(),
            mapOf("query1" to queryParameter))

        assertThat(result).isEqualTo(mapOf("query1" to DEFAULT_PARAM_VALUE))
    }

    @Test
    fun `should be able to handle default values of non-string type defined in @QueryParam`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)
        every { request.queryStringParameters } returns null

        val queryParameter = TestController::class.members
            .find { it.name == "endpointWithDefaultNonStringQueryParam" }!!
            .parameters.find { it.name == "queryParam" }!!

        val result = QueryParamValueInjector.injectParamValues(
            request,
            mockk(),
            mapOf("query1" to queryParameter))

        assertThat(result).isEqualTo(mapOf("query1" to DEFAULT_NON_STRING_PARAM_VALUE.toInt()))
    }

    @Test
    fun `should be able to handle array @QueryParam parameters`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)
        val queryParamValues = listOf("value1", "value2")
        every { request.multiValueQueryStringParameters } returns mapOf("query1" to queryParamValues)

        val queryParameter = TestController::class.members
            .find { it.name == "endpointWithMultiKeyQueryParam" }!!
            .parameters.find { it.name == "queryParam" }!!

        val result = QueryParamValueInjector.injectParamValues(
            request,
            mockk(),
            mapOf("query1" to queryParameter))

        assertThat(result["query1"]).isEqualTo(queryParamValues.toTypedArray())
    }


    @Test
    fun `should merge single and multi key query params`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)
        val multiKeyQueryParamValues = listOf("value1", "value2")
        every { request.multiValueQueryStringParameters } returns mapOf("query1" to multiKeyQueryParamValues)

        val singleKeyQueryParamValue = "singleKeyValud"
        every { request.queryStringParameters } returns mapOf("query2" to singleKeyQueryParamValue)

        val multiKeyQueryParameter = TestController::class.members
            .find { it.name == "endpointWithSingleAndMultiKeyQueryParam" }!!
            .parameters.find { it.name == "queryParam" }!!

        val singleKeyQueryParameter = TestController::class.members
            .find { it.name == "endpointWithSingleAndMultiKeyQueryParam" }!!
            .parameters.find { it.name == "queryParam2" }!!

        val result = QueryParamValueInjector.injectParamValues(
            request,
            mockk(),
            mapOf("query1" to multiKeyQueryParameter, "query2" to singleKeyQueryParameter))

        assertThat(result["query1"]).isEqualTo(multiKeyQueryParamValues.toTypedArray())
        assertThat(result["query2"]).isEqualTo(singleKeyQueryParamValue)

    }


    private class TestController {

        @Endpoint("test", method = HttpMethod.GET)
        fun endpoint(@QueryParam("query1") queryParam: String): HttpResponse<String> {
            return HttpResponse.ok(queryParam)
        }

        @Endpoint("test", method = HttpMethod.GET)
        fun endpointWithDefaultQueryParam(
            @QueryParam(
                "query1",
                defaultValue = DEFAULT_PARAM_VALUE) queryParam: String): HttpResponse<String> {
            return HttpResponse.ok(queryParam)
        }

        @Endpoint("test", method = HttpMethod.GET)
        fun endpointWithDefaultNonStringQueryParam(
            @QueryParam(
                "query1",
                defaultValue = DEFAULT_NON_STRING_PARAM_VALUE) queryParam: Int): HttpResponse<String> {
            return HttpResponse.ok("$queryParam")
        }

        @Endpoint("test", method = HttpMethod.GET)
        fun endpointWithMultiKeyQueryParam(@QueryParam("query1") queryParam: Array<String>): HttpResponse<Array<String>> {
            return HttpResponse.ok(queryParam)
        }

        @Endpoint("test", method = HttpMethod.GET)
        fun endpointWithSingleAndMultiKeyQueryParam(
            @QueryParam("query1") queryParam: Array<String>,
            @QueryParam("query2") queryParam2: String): HttpResponse<Array<String>> {
            return HttpResponse.ok(queryParam)
        }
    }
}
