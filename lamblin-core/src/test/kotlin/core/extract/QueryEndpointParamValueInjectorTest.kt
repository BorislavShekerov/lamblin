/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.extract.QueryEndpointParamValueInjector
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.QueryParam
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

const val DEFAULT_PARAM_VALUE = "default"

class QueryEndpointParamValueInjectorTest {

    @Test
    fun `should return the value for each query param`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)
        every { request.queryStringParameters } returns mapOf("query1" to "value1")

        val result = QueryEndpointParamValueInjector.injectParamValues(
                request,
                mockk(),
                mapOf("query1" to TestController::class.java.declaredMethods
                        .find { it.name === "endpoint" }!!
                        .parameters[0]))

        assertThat(result).isEqualTo(mapOf("query1" to "value1"))
    }

    @Test
    fun `should return empty map if no query string params and default value not defined in @QueryParam`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)
        every { request.queryStringParameters } returns null

        val result = QueryEndpointParamValueInjector.injectParamValues(
                request,
                mockk(),
                mapOf("query1" to TestController::class.java.declaredMethods
                        .find { it.name === "endpoint" }!!
                        .parameters[0]))

        assertThat(result).isEqualTo(mapOf("query1" to null))
    }

    @Test
    fun `should return default value if query param not present and default value defined in @QuaryParam`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)
        every { request.queryStringParameters } returns null

        val result = QueryEndpointParamValueInjector.injectParamValues(
                request,
                mockk(),
                mapOf("query1" to TestController::class.java.declaredMethods
                        .find { it.name === "endpointWithDefaultQueryParam" }!!
                        .parameters[0]))

        assertThat(result).isEqualTo(mapOf("query1" to DEFAULT_PARAM_VALUE))
    }

    private class TestController {

        @Endpoint("test", method = HttpMethod.GET)
        fun endpoint(@QueryParam("query1") queryParam: String): HttpResponse<String> {
            return HttpResponse.ok(queryParam)
        }

        @Endpoint("test", method = HttpMethod.GET)
        fun endpointWithDefaultQueryParam(@QueryParam("query1", defaultValue = DEFAULT_PARAM_VALUE) queryParam: String): HttpResponse<String> {
            return HttpResponse.ok(queryParam)
        }
    }
}
