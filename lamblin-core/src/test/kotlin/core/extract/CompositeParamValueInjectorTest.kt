/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.extract.*
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
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.reflect.KCallable
import kotlin.reflect.full.valueParameters

class CompositeParamValueInjectorTest {

    private val queryParamValueInjector: QueryParamValueInjector = mockk(relaxed = true)
    private val pathParamValueInjector: PathParamValueInjector = mockk(relaxed = true)
    private val endpointParamValueInjectorComposite = CompositeParamValueInjector(
        listOf(queryParamValueInjector, pathParamValueInjector)
    )

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
                method = TestController::class.members.find { it.name == "endpointNoParams" } as KCallable<HttpResponse<*>>,
                controllerClass = TestController::class),
            mapOf())

        assertThat(result).isEmpty()
    }

    @Test
    fun `instance should contain all value injectors`() {
        val instance = CompositeParamValueInjector.instance()

        assertThat(instance.injectorEndpoints).hasSize(5)
        assertThat(instance.injectorEndpoints).contains(PathParamValueInjector)
        assertThat(instance.injectorEndpoints).contains(QueryParamValueInjector)
        assertThat(instance.injectorEndpoints).contains(RequestBodyParamValueInjector)
        assertThat(instance.injectorEndpoints).contains(HeaderParamValueInjector)
        assertThat(instance.injectorEndpoints).contains(ApiGatewayRequestParamValueInjector)
    }

    @Test
    fun `should return ordered param value for each param`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)
        val method = TestController::class.members.find { it.name == "endpointWithParam" }

        val handlerMethod = HandlerMethod(
            "endpointWithParam/{pathParam}",
            HttpMethod.GET,
            mapOf(
                "queryParam" to HandlerMethodParameter(
                    annotationMappedName = "queryParam",
                    name = "queryParam",
                    type = String::class),
                "pathParam" to HandlerMethodParameter(
                    annotationMappedName = "pathParam",
                    name = "pathParam",
                    type = String::class)),
            method as KCallable<HttpResponse<*>>,
            TestController::class)


        val paramAnnotationMappedNameToParam = mapOf(
            "queryParam" to method.valueParameters[0],
            "pathParam" to method.valueParameters[0])

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
        val method = TestController::class.members.find { it.name == "endpointWithParamsFlipped" }!!

        val handlerMethod = HandlerMethod(
            "endpointWithParam/{pathParam}",
            HttpMethod.GET,
            mapOf(
                "pathParam" to HandlerMethodParameter(
                    annotationMappedName = "pathParam",
                    name = "pathParam",
                    type = String::class),
                "queryParam" to HandlerMethodParameter(
                    annotationMappedName = "queryParam",
                    name = "queryParam",
                    type = String::class)),
            method as KCallable<HttpResponse<*>>,
            TestController::class)


        val paramAnnotationMappedNameToParam = mapOf(
            "queryParam" to method.valueParameters[1],
            "pathParam" to method.valueParameters[0])

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

    @Test
    fun `should throw IllegalStateException if param not found for a give name`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)
        val method = TestController::class.members.find { it.name == "endpointWithParamsFlipped" }!!

        val handlerMethod = HandlerMethod(
            "endpointWithParam/{pathParam}",
            HttpMethod.GET,
            mapOf(
                "queryParam" to HandlerMethodParameter(
                    annotationMappedName = "queryParam",
                    name = "queryParam",
                    type = String::class)),
            method as KCallable<HttpResponse<*>>,
            TestController::class)


        val paramAnnotationMappedNameToParam = mapOf(
            "queryParam" to method.valueParameters[1],
            "pathParam" to method.valueParameters[0])

        assertThrows<IllegalStateException> {
            endpointParamValueInjectorComposite.injectParamValues(
                request,
                handlerMethod,
                paramAnnotationMappedNameToParam)
        }
    }

    private class TestController {

        @Endpoint("endpointNoParams", method = HttpMethod.GET)
        fun endpointNoParams(): HttpResponse<String> {
            return HttpResponse.ok("")
        }

        @Endpoint("endpointWithParam/{pathParam}", method = HttpMethod.GET)
        fun endpointWithParam(
            @QueryParam("queryParam") queryParam: String,
            @PathParam("pathParam") pathParam: String
        ): HttpResponse<String> {

            return HttpResponse.ok(queryParam + pathParam)
        }

        @Endpoint("endpointWithParam/{pathParam}", method = HttpMethod.GET)
        fun endpointWithParamsFlipped(
            @PathParam("pathParam") pathParam: String,
            @QueryParam("queryParam") queryParam: String
        ): HttpResponse<String> {

            return HttpResponse.ok(pathParam + queryParam)
        }
    }
}
