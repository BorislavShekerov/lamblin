package com.lamblin.core

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.fasterxml.jackson.annotation.JsonProperty
import com.lamblin.core.exception.RequestPayloadParseException
import com.lamblin.core.extract.DefaultBodyJsonDeserializer
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.annotation.RequestBody
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultBodyJsonDeserializerTest {

    @Test
    fun `should be able to deserialize request body if handler POST, RequestBody method param present and request body not empty `() {
        assertTrue {
            DefaultBodyJsonDeserializer.canDeserializeRequestBody(
                    HandlerMethod(
                            "test",
                            HttpMethod.POST,
                            method = TestController::class.java.declaredMethods.first(),
                            controllerClass = TestController::class.java),
                    APIGatewayProxyRequestEvent().apply {
                        body = """{ "body": "test" }"""
                    })
        }
    }

    @Test
    fun `should not be able to deserialize request body if handler GET`() {
        assertFalse {
            DefaultBodyJsonDeserializer.canDeserializeRequestBody(
                    HandlerMethod(
                            "test",
                            HttpMethod.GET,
                            method = TestController::class.java.declaredMethods.first(),
                            controllerClass = TestController::class.java),
                    APIGatewayProxyRequestEvent().apply {
                        body = """{ "body": "test" }"""
                    })
        }
    }

    @Test
    fun `should not be able to deserialize request body if no params in endpoint`() {
        assertFalse {
            DefaultBodyJsonDeserializer.canDeserializeRequestBody(
                    HandlerMethod(
                            "test",
                            HttpMethod.GET,
                            method = TestController::class.java.declaredMethods[1],
                            controllerClass = TestController::class.java),
                    APIGatewayProxyRequestEvent().apply {
                        body = """{ "body": "test" }"""
                    })
        }
    }

    @Test
    fun `should not be able to deserialize request body if RequestBody annotation not present`() {
        assertFalse {
            DefaultBodyJsonDeserializer.canDeserializeRequestBody(
                    HandlerMethod(
                            "test",
                            HttpMethod.POST,
                            method = TestController::class.java.declaredMethods[2],
                            controllerClass = TestController::class.java),
                    APIGatewayProxyRequestEvent().apply {
                        body = """{ "body": "test" }"""
                    })
        }
    }

    @Test
    fun `should not be able to deserialize request body if request body empty`() {
        assertFalse {
            DefaultBodyJsonDeserializer.canDeserializeRequestBody(
                    HandlerMethod(
                            "test",
                            HttpMethod.POST,
                            method = TestController::class.java.declaredMethods[2],
                            controllerClass = TestController::class.java),
                    APIGatewayProxyRequestEvent())
        }
    }

    @Test
    fun `should throw RequestPayloadParseException when payload JSON invalid`() {
        assertThrows<RequestPayloadParseException> {
                        DefaultBodyJsonDeserializer.deserializeBodyJson(
                                TestController::class.java.methods[0].parameters,
                                "{'foo':")
        }
    }

    @Test
    fun `should be able to deserialize body when body json valid and matches param type`() {
        val result = DefaultBodyJsonDeserializer.deserializeBodyJson(
                TestController::class.java.methods[0].parameters,
                """{ "result" : "test" }""")

        assertThat(result.first).isEqualTo("arg0")
        assertThat(result.second).isEqualTo(RequestBodySample("test"))
    }

    class TestController {

        fun validEndpoint(@RequestBody requestBody: RequestBodySample) = {
        }

        fun noParamsEndpoint() {
        }

        fun missingRequestBodyAnnotationEndpoint(requestBody: Any) {
        }
    }

    data class RequestBodySample constructor(@JsonProperty("result") val result: String)
}