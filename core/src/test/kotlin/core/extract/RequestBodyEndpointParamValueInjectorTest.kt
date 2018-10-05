package core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.fasterxml.jackson.annotation.JsonProperty
import com.lamblin.core.exception.RequestPayloadParseException
import com.lamblin.core.extract.RequestBodyEndpointParamValueInjector
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.REQUEST_BODY_MAPPED_NAME
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.RequestBody
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

const val NO_BODY_PARAM_PATH = "no-body"
const val BODY_PARAM_PRESENT_PATH = "body-param-present"

class RequestBodyEndpointParamValueInjectorTest {

    @Test
    fun `should return empty map when request not post`() {
        val handlerMethod: HandlerMethod = mockk()

        every { handlerMethod.httpMethod } returns HttpMethod.GET

        val result = RequestBodyEndpointParamValueInjector.injectParamValues(mockk(), handlerMethod, mapOf())
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty map when no params`() {
        val noBodyParamMethod = TestController::class.java.declaredMethods.find { it.name === "endpointNoBodyParam" }!!
        val handlerMethod = HandlerMethod(
                NO_BODY_PARAM_PATH,
                HttpMethod.POST,
                mapOf(),
                noBodyParamMethod,
                TestController::class.java)

        val result = RequestBodyEndpointParamValueInjector.injectParamValues(mockk(), handlerMethod, mapOf())
        assertThat(result).isEmpty()
    }

    @Test
    fun `should return empty map if request body empty and RequestBody param present`() {
        val bodyParamMethod = TestController::class.java.declaredMethods.find { it.name === "endpointWithBodyParam" }!!
        val handlerMethod = HandlerMethod(
                NO_BODY_PARAM_PATH,
                HttpMethod.POST,
                mapOf(),
                bodyParamMethod,
                TestController::class.java)

        val result = RequestBodyEndpointParamValueInjector.injectParamValues(mockk(relaxed = true), handlerMethod, mapOf())
        assertThat(result).isEmpty()
    }

    @Test
    fun `should throw RequestPayloadParseException exception when JSON malformed`() {
        val bodyParamMethod = TestController::class.java.declaredMethods.find { it.name === "endpointWithBodyParam" }!!
        val handlerMethod = HandlerMethod(
                NO_BODY_PARAM_PATH,
                HttpMethod.POST,
                mapOf(),
                bodyParamMethod,
                TestController::class.java)

        val request: APIGatewayProxyRequestEvent = mockk()
        every { request.body } returns """{ "content": }"""

        assertThrows<RequestPayloadParseException> {
            RequestBodyEndpointParamValueInjector.injectParamValues(
                    request,
                    handlerMethod,
                    mapOf(REQUEST_BODY_MAPPED_NAME to bodyParamMethod.parameters[0]))
        }
    }

    @Test
    fun `should deserialize body when request POST, RequestBody param present, request body present and JSON correct`() {
        verifyThatRequestBodyWasExtractedFromValidRequest(HttpMethod.POST)
    }

    @Test
    fun `should deserialize body when request PUT, RequestBody param present, request body present and JSON correct`() {
        verifyThatRequestBodyWasExtractedFromValidRequest(HttpMethod.PUT)
    }

    @Test
    fun `should deserialize body when request PATCH, RequestBody param present, request body present and JSON correct`() {
        verifyThatRequestBodyWasExtractedFromValidRequest(HttpMethod.PATCH)
    }

    private fun verifyThatRequestBodyWasExtractedFromValidRequest(httpMethod: HttpMethod) {
        val bodyParamMethod = TestController::class.java.declaredMethods.find { it.name === "endpointWithBodyParam" }!!
        val handlerMethod = HandlerMethod(
                NO_BODY_PARAM_PATH,
                httpMethod,
                mapOf(),
                bodyParamMethod,
                TestController::class.java)

        val request: APIGatewayProxyRequestEvent = mockk()
        every { request.body } returns """{ "content": "test"}"""

        val result = RequestBodyEndpointParamValueInjector.injectParamValues(
                request,
                handlerMethod,
                mapOf(REQUEST_BODY_MAPPED_NAME to bodyParamMethod.parameters[0]))

        assertThat(result).isEqualTo(mapOf(REQUEST_BODY_MAPPED_NAME to TestBody(
                "test")))
    }

    private data class TestBody(@JsonProperty val content: String)

    private class TestController {

        @Endpoint(NO_BODY_PARAM_PATH, method = HttpMethod.POST)
        fun endpointNoBodyParam(): HttpResponse<String> {
            return HttpResponse.ok("")
        }

        @Endpoint(BODY_PARAM_PRESENT_PATH, method = HttpMethod.POST)
        fun endpointWithBodyParam(@RequestBody body: TestBody): HttpResponse<String> {
            return HttpResponse.ok(body.toString())
        }
    }
}
