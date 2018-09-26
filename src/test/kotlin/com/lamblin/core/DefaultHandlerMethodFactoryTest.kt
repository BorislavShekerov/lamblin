package com.lamblin.core

import com.lamblin.core.model.HandlerMethodParameter
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import com.lamblin.core.model.annotation.RequestBody
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.reflect.Method

const val QUERY_PARAM_NAME = "queryParam"
const val PATH_PARAM_NAME = "pathParam"

class DefaultHandlerMethodFactoryTest {

    @Test
    fun `method has no endpoint annotation`() {
        assertThrows<IllegalArgumentException> {
            DefaultHandlerMethodFactory.method(
                    TestController::class.java.declaredMethods.first(),
                    TestController::class.java)
        }
    }

    @Test
    fun `should create GET method handler when endpoint method GET and no params`() {
        verifyCorrectMethodHandlerCreated(
                TestController::class.java.declaredMethods[1],
                HttpMethod.GET,
                path = "path")
    }

    @Test
    fun `should create GET method handler when endpoint method GET with path param`() {
        verifyCorrectMethodHandlerCreated(
                TestController::class.java.declaredMethods[2],
                HttpMethod.GET,
                path = "path/$PATH_PARAM_NAME",
                paramNameToParam = mapOf("arg0" to HandlerMethodParameter(
                        annotationMappedName = PATH_PARAM_NAME,
                        name = "arg0",
                        type = String::class.java)))
    }

    @Test
    fun `should create GET method handler when endpoint method GET with query param`() {
        verifyCorrectMethodHandlerCreated(
                TestController::class.java.declaredMethods[3],
                HttpMethod.GET,
                paramNameToParam = mapOf("arg0" to HandlerMethodParameter(
                        annotationMappedName = QUERY_PARAM_NAME,
                        required = true,
                        name = "arg0",
                        type = String::class.java)))
    }

    @Test
    fun `should create GET method handler when endpoint method GET with query and path params`() {
        verifyCorrectMethodHandlerCreated(
                TestController::class.java.declaredMethods[4],
                HttpMethod.GET,
                path = "path/$PATH_PARAM_NAME",
                paramNameToParam = mapOf(
                        "arg0" to HandlerMethodParameter(
                                annotationMappedName = PATH_PARAM_NAME,
                                name = "arg0",
                                type = String::class.java),
                        "arg1" to HandlerMethodParameter(
                                annotationMappedName = QUERY_PARAM_NAME,
                                required = true,
                                name = "arg1",
                                type = String::class.java)))
    }

    @Test
    fun `should create POST method handler when endpoint method POST without body`() {
        verifyCorrectMethodHandlerCreated(
                TestController::class.java.declaredMethods[5],
                HttpMethod.POST)
    }

    @Test
    fun `should create POST method handler when endpoint method POST with request body`() {
        verifyCorrectMethodHandlerCreated(
                TestController::class.java.declaredMethods[6],
                HttpMethod.POST,
                path = "path",
                paramNameToParam = mapOf("arg0" to HandlerMethodParameter(
                        name = "arg0",
                        type = Any::class.java)))
    }

    @Test
    fun `should create PATCH method handler when endpoint method PATCH without body`() {
        verifyCorrectMethodHandlerCreated(
                TestController::class.java.declaredMethods[7],
                HttpMethod.PATCH)
    }

    @Test
    fun `should create PATCH method handler when endpoint method PATCH with request body`() {
        verifyCorrectMethodHandlerCreated(
                TestController::class.java.declaredMethods[8],
                HttpMethod.PATCH,
                path = "path",
                paramNameToParam = mapOf("arg0" to HandlerMethodParameter(
                        name = "arg0",
                        type = Any::class.java)))
    }

    @Test
    fun `should create DELETE method handler when endpoint method DELETE`() {
        verifyCorrectMethodHandlerCreated(
                TestController::class.java.declaredMethods[9],
                HttpMethod.DELETE)
    }

    private fun verifyCorrectMethodHandlerCreated(endpointMethod: Method,
                                                  httpMethod: HttpMethod,
                                                  path: String = "path",
                                                  paramNameToParam: Map<String, HandlerMethodParameter> = mapOf()) {

        val handlerMethod = DefaultHandlerMethodFactory.method(
                endpointMethod,
                TestController::class.java)

        assertThat(handlerMethod.controllerClass).isEqualTo(TestController::class.java)
        assertThat(handlerMethod.method).isEqualTo(endpointMethod)
        assertThat(handlerMethod.httpMethod).isEqualTo(httpMethod)
        assertThat(handlerMethod.path).isEqualTo(path)
        assertThat(handlerMethod.paramNameToParam).isEqualTo(paramNameToParam)
    }

    class TestController {

        fun testEndpointNoAnnotation(): HttpResponse<Void> {
            return HttpResponse()
        }

        @Endpoint(path = "path", method = HttpMethod.GET)
        fun testEndpointGetAnnotationNoParams(): HttpResponse<Void> {
            return HttpResponse()
        }

        @Endpoint(path = "path/$PATH_PARAM_NAME", method = HttpMethod.GET)
        fun testEndpointGetQueryParams(@PathParam(PATH_PARAM_NAME) pathParam: String): HttpResponse<Void> {
            return HttpResponse()
        }

        @Endpoint(path = "path", method = HttpMethod.GET)
        fun testEndpointGetPathParams(@QueryParam(QUERY_PARAM_NAME) queryParam: String): HttpResponse<Void> {
            return HttpResponse()
        }

        @Endpoint(path = "path/$PATH_PARAM_NAME", method = HttpMethod.GET)
        fun testEndpointGetQueryAndPathParams(
                @PathParam(PATH_PARAM_NAME) pathParam: String,
                @QueryParam(QUERY_PARAM_NAME) queryParam: String): HttpResponse<Void> {

            return HttpResponse()
        }

        @Endpoint(path = "path", method = HttpMethod.POST)
        fun testEndpointPostNoBody(): HttpResponse<Void> {
            return HttpResponse()
        }

        @Endpoint(path = "path", method = HttpMethod.POST)
        fun testEndpointPostWithBody(@RequestBody body: Any): HttpResponse<Void> {
            return HttpResponse()
        }

        @Endpoint(path = "path", method = HttpMethod.PATCH)
        fun testEndpointPatchNoBody(): HttpResponse<Void> {
            return HttpResponse()
        }

        @Endpoint(path = "path", method = HttpMethod.PATCH)
        fun testEndpointPatchWithBody(@RequestBody body: Any): HttpResponse<Void> {
            return HttpResponse()
        }

        @Endpoint(path = "path", method = HttpMethod.DELETE)
        fun testEndpointDelete(): HttpResponse<Void> {
            return HttpResponse()
        }
    }
}