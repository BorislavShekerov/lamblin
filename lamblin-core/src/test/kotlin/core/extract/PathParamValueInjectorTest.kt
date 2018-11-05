/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.extract.PathParamValueInjector
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.QueryParam
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

const val PATH_PARAM_ENDPOINT_PATH = "/path/{param}"

class PathParamValueInjectorTest {

    @Test
    fun `should return empty map if no path parameters in handler method`() {
        val request: APIGatewayProxyRequestEvent = mockk()
        val handlerMethod: HandlerMethod = mockk()

        every { handlerMethod.path } returns "/path"
        every { request.path } returns "/path"

        val result = PathParamValueInjector.injectParamValues(request, handlerMethod, mapOf())
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return the value for each path param on`() {
        val request: APIGatewayProxyRequestEvent = mockk()
        val handlerMethod: HandlerMethod = mockk()

        every { handlerMethod.path } returns PATH_PARAM_ENDPOINT_PATH
        every { request.path } returns "/path/paramValue"

        val pathParamParameter = TestController::class.members.first().parameters[0]

        val result = PathParamValueInjector.injectParamValues(
            request,
            handlerMethod,
            mapOf("param" to pathParamParameter))
        assertThat(result).isEqualTo(mapOf("param" to "paramValue"))
    }

    private class TestController {

        @Endpoint(PATH_PARAM_ENDPOINT_PATH, method = HttpMethod.GET)
        fun endpoint(@QueryParam("param") pathParam: String): HttpResponse<String> {
            return HttpResponse.ok(pathParam)
        }
    }
}
