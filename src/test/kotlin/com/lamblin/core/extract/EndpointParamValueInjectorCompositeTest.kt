package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HandlerMethodParameter
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EndpointParamValueInjectorCompositeTest {

    private val queryParamValueInjector: QueryEndpointParamValueInjector = mockk(relaxed = true)
    private val pathParamValueInjector: PathParamEndpointValueInjector = mockk(relaxed = true)
    private val endpointParamValueInjectorComposite = EndpointParamValueInjectorComposite(
            listOf(queryParamValueInjector, pathParamValueInjector))

    @BeforeEach
    fun setUp() {
        clearMocks(queryParamValueInjector, pathParamValueInjector)
    }

    @Test
    fun `should return empty result if no params`() {
        val result = endpointParamValueInjectorComposite.injectParamValues(
                mockk(relaxed = true),
                HandlerMethod(
                        "",
                        HttpMethod.GET,
                        method = TestController::class.java.declaredMethods.find { it.name === "endpointNoParams" }!!,
                        controllerClass = TestController::class.java),
                mapOf())

        assertThat(result).isEmpty()
    }

    @Test
    fun `should return ordered param value for each param`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)
        val method = TestController::class.java.declaredMethods.find { it.name === "endpointWithParam" }!!

        val handlerMethod = HandlerMethod(
                "endpointWithParam/{pathParam}",
                HttpMethod.GET,
                mapOf(
                        "arg0" to HandlerMethodParameter(
                                annotationMappedName = "queryParam",
                                name = "arg0",
                                type = String::class.java),
                        "arg1" to HandlerMethodParameter(
                                annotationMappedName = "pathParam",
                                name = "arg1",
                                type = String::class.java)),
                method,
                TestController::class.java)


        val paramAnnotationMappedNameToParam = mapOf(
                "queryParam" to method.parameters[0],
                "pathParam" to method.parameters[0])

        every {
            queryParamValueInjector.injectParamValues(request, handlerMethod, paramAnnotationMappedNameToParam)
        } returns mapOf("queryParam" to "queryValue")
        every {
            pathParamValueInjector.injectParamValues(request, handlerMethod, paramAnnotationMappedNameToParam)
        } returns mapOf("pathParam" to "pathValue")

        val result = endpointParamValueInjectorComposite.injectParamValues(
                request,
                handlerMethod,
                paramAnnotationMappedNameToParam)

        assertThat(result).isEqualTo(linkedMapOf("queryParam" to "queryValue", "pathParam" to "pathValue"))
    }

    @Test
    fun `should return ordered param value for each param, flipped params`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)
        val method = TestController::class.java.declaredMethods.find { it.name === "endpointWithParamsFlipped" }!!

        val handlerMethod = HandlerMethod(
                "endpointWithParam/{pathParam}",
                HttpMethod.GET,
                mapOf(
                        "arg0" to HandlerMethodParameter(
                                annotationMappedName = "pathParam",
                                name = "arg1",
                                type = String::class.java),
                        "arg1" to HandlerMethodParameter(
                                annotationMappedName = "queryParam",
                                name = "arg0",
                                type = String::class.java)),
                method,
                TestController::class.java)


        val paramAnnotationMappedNameToParam = mapOf(
                "queryParam" to method.parameters[0],
                "pathParam" to method.parameters[1])

        every {
            queryParamValueInjector.injectParamValues(request, handlerMethod, paramAnnotationMappedNameToParam)
        } returns mapOf("queryParam" to "queryValue")
        every {
            pathParamValueInjector.injectParamValues(request, handlerMethod, paramAnnotationMappedNameToParam)
        } returns mapOf("pathParam" to "pathValue")

        val result = endpointParamValueInjectorComposite.injectParamValues(
                request,
                handlerMethod,
                paramAnnotationMappedNameToParam)

        assertThat(result).isEqualTo(linkedMapOf("pathParam" to "pathValue", "queryParam" to "queryValue"))
    }

    private class TestController {

        @Endpoint("endpointNoParams", method = HttpMethod.GET)
        fun endpointNoParams(): HttpResponse<String> {
            return HttpResponse.ok("")
        }

        @Endpoint("endpointWithParam/{pathParam}", method = HttpMethod.GET)
        fun endpointWithParam(
                @QueryParam("queryParam") queryParam: String,
                @PathParam("pathParam") pathParam: String): HttpResponse<String> {

            return HttpResponse.ok(queryParam + pathParam)
        }

        @Endpoint("endpointWithParam/{pathParam}", method = HttpMethod.GET)
        fun endpointWithParamsFlipped(
                @PathParam("pathParam") pathParam: String,
                @QueryParam("queryParam") queryParam: String): HttpResponse<String> {

            return HttpResponse.ok(pathParam + queryParam)
        }
    }
}
