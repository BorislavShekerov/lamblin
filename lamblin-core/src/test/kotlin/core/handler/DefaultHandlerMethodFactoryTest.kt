/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package core.handler

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.handler.DefaultHandlerMethodFactory
import com.lamblin.core.model.HandlerMethodParameter
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import com.lamblin.core.model.annotation.RequestBody
import com.lamblin.core.security.AccessControl
import com.lamblin.core.security.RequestAuthorizer
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.reflect.Method

const val QUERY_PARAM_NAME = "queryParam"
const val PATH_PARAM_NAME = "pathParam"

class DefaultHandlerMethodFactoryTest {

    @Test
    fun `method has no endpoint annotation`() {
        assertThrows<IllegalStateException> {
            DefaultHandlerMethodFactory.method(
                TestController::class.java.declaredMethods.find { it.name === "testEndpointNoAnnotation" }!!,
                TestController::class.java)
        }
    }

    @Test
    fun `should create GET method handler when endpoint method GET and no params`() {
        verifyCorrectMethodHandlerCreated(
            TestController::class.java.declaredMethods.find { it.name === "testEndpointGetAnnotationNoParams" }!!,
            HttpMethod.GET,
            path = "path")
    }

    @Test
    fun `should create GET method handler when endpoint method GET with path param`() {
        verifyCorrectMethodHandlerCreated(
            TestController::class.java.declaredMethods.find { it.name === "testEndpointGetQueryParams" }!!,
            HttpMethod.GET,
            path = "path/$PATH_PARAM_NAME",
            paramNameToParam = mapOf(
                "arg0" to HandlerMethodParameter(
                    annotationMappedName = PATH_PARAM_NAME,
                    name = "arg0",
                    type = String::class.java)))
    }

    @Test
    fun `should create GET method handler when endpoint method GET with query param`() {
        verifyCorrectMethodHandlerCreated(
            TestController::class.java.declaredMethods.find { it.name === "testEndpointGetPathParams" }!!,
            HttpMethod.GET,
            paramNameToParam = mapOf(
                "arg0" to HandlerMethodParameter(
                    annotationMappedName = QUERY_PARAM_NAME,
                    required = true,
                    name = "arg0",
                    type = String::class.java)))
    }

    @Test
    fun `should create GET method handler when endpoint method GET with query and path params`() {
        verifyCorrectMethodHandlerCreated(
            TestController::class.java.declaredMethods.find { it.name === "testEndpointGetQueryAndPathParams" }!!,
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
            TestController::class.java.declaredMethods.find { it.name === "testEndpointPostNoBody" }!!,
            HttpMethod.POST
        )
    }

    @Test
    fun `should create POST method handler when endpoint method POST with request body`() {
        verifyCorrectMethodHandlerCreated(
            TestController::class.java.declaredMethods.find { it.name === "testEndpointPostWithBody" }!!,
            HttpMethod.POST,
            path = "path",
            paramNameToParam = mapOf(
                "arg0" to HandlerMethodParameter.requestBodyParam(
                    name = "arg0",
                    type = Any::class.java)))
    }

    @Test
    fun `should create PATCH method handler when endpoint method PATCH without body`() {
        verifyCorrectMethodHandlerCreated(
            TestController::class.java.declaredMethods.find { it.name === "testEndpointPatchNoBody" }!!,
            HttpMethod.PATCH)
    }

    @Test
    fun `should create PATCH method handler when endpoint method PATCH with request body`() {
        verifyCorrectMethodHandlerCreated(
            TestController::class.java.declaredMethods.find { it.name === "testEndpointPatchWithBody" }!!,
            HttpMethod.PATCH,
            path = "path",
            paramNameToParam = mapOf(
                "arg0" to HandlerMethodParameter.requestBodyParam(
                    name = "arg0",
                    type = Any::class.java)))
    }

    @Test
    fun `should create DELETE method handler when endpoint method DELETE`() {
        verifyCorrectMethodHandlerCreated(
            TestController::class.java.declaredMethods.find { it.name === "testEndpointDelete" }!!,
            HttpMethod.DELETE)
    }

    @Test
    fun `should create set handler AccessControl when @AccessControlPresentOnRequest`() {
        val accessControlMethod =
            TestController::class.java.declaredMethods.find { it.name === "testAccessControlEndpoint" }!!

        verifyCorrectMethodHandlerCreated(
            accessControlMethod,
            HttpMethod.DELETE,
            accessControl =accessControlMethod.annotations.find { it is AccessControl } as AccessControl)
    }

    private fun verifyCorrectMethodHandlerCreated(
        endpointMethod: Method,
        httpMethod: HttpMethod,
        path: String = "path",
        paramNameToParam: Map<String, HandlerMethodParameter> = mapOf(),
        accessControl: AccessControl? = null) {

        val handlerMethod = DefaultHandlerMethodFactory.method(
            endpointMethod,
            TestController::class.java
        )

        assertThat(handlerMethod.controllerClass).isEqualTo(
            TestController::class.java
        )
        assertThat(handlerMethod.method).isEqualTo(endpointMethod)
        assertThat(handlerMethod.httpMethod).isEqualTo(httpMethod)
        assertThat(handlerMethod.path).isEqualTo(path)
        assertThat(handlerMethod.paramNameToParam).isEqualTo(paramNameToParam)
        assertThat(handlerMethod.accessControl).isEqualTo(accessControl)
    }

    private class TestController {

        fun testEndpointNoAnnotation(): HttpResponse<Void> {
            return HttpResponse()
        }

        @Endpoint(path = "path", method = HttpMethod.GET)
        fun testEndpointGetAnnotationNoParams(): HttpResponse<Void> {
            return HttpResponse()
        }

        @Endpoint(path = "path/$PATH_PARAM_NAME", method = HttpMethod.GET)
        fun testEndpointGetQueryParams(@PathParam(PATH_PARAM_NAME) pathParam: String): HttpResponse<String> {
            return HttpResponse.ok(pathParam)
        }

        @Endpoint(path = "path", method = HttpMethod.GET)
        fun testEndpointGetPathParams(@QueryParam(QUERY_PARAM_NAME) queryParam: String): HttpResponse<String> {
            return HttpResponse.ok(queryParam)
        }

        @Endpoint(path = "path/$PATH_PARAM_NAME", method = HttpMethod.GET)
        fun testEndpointGetQueryAndPathParams(
            @PathParam(PATH_PARAM_NAME) pathParam: String,
            @QueryParam(QUERY_PARAM_NAME) queryParam: String
        ): HttpResponse<String> {

            return HttpResponse.ok(pathParam + queryParam)
        }

        @Endpoint(path = "path", method = HttpMethod.POST)
        fun testEndpointPostNoBody(): HttpResponse<Void> {
            return HttpResponse()
        }

        @Endpoint(path = "path", method = HttpMethod.POST)
        fun testEndpointPostWithBody(@RequestBody body: Any): HttpResponse<String> {
            return HttpResponse.ok(body.toString())
        }

        @Endpoint(path = "path", method = HttpMethod.PATCH)
        fun testEndpointPatchNoBody(): HttpResponse<Void> {
            return HttpResponse()
        }

        @Endpoint(path = "path", method = HttpMethod.PATCH)
        fun testEndpointPatchWithBody(@RequestBody body: Any): HttpResponse<String> {
            return HttpResponse.ok(body.toString())
        }

        @Endpoint(path = "path", method = HttpMethod.DELETE)
        fun testEndpointDelete(): HttpResponse<Void> {
            return HttpResponse()
        }

        @AccessControl(["test"], Authorizer::class)
        @Endpoint(path = "path", method = HttpMethod.DELETE)
        fun testAccessControlEndpoint(): HttpResponse<Void> {
            return HttpResponse()
        }
    }

    class Authorizer: RequestAuthorizer {

        override fun isRequestAuthorized(roles: Array<String>, request: APIGatewayProxyRequestEvent): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}
