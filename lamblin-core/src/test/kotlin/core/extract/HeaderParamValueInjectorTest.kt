package core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.extract.HeaderParamValueInjector
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.Header
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private const val HEADER_NAME = "Authorization"

class HeaderParamValueInjectorTest {

    @Test
    fun `should inject header params`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)

        val headerValue = "Bearer xxx"

        every { request.headers } returns mapOf(HEADER_NAME to headerValue)

        val headerParameter = TestController::class.members
            .find { it.name == "endpoint" }!!
            .parameters.find { it.name == "authorizationHeader" }!!

        val result = HeaderParamValueInjector.injectParamValues(
            request,
            mockk(),
            mapOf(HEADER_NAME to headerParameter))

        assertThat(result).containsKey(HEADER_NAME)
        assertThat(result[HEADER_NAME]).isEqualTo(headerValue)
    }

    @Test
    fun `should inject null for missing header params`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)
        every { request.headers } returns mapOf("foo" to "bar")

        val headerParameter = TestController::class.members
            .find { it.name == "endpoint" }!!
            .parameters.find { it.name == "authorizationHeader" }!!

        val result = HeaderParamValueInjector.injectParamValues(
            request,
            mockk(),
            mapOf(HEADER_NAME to headerParameter))

        assertThat(result).containsKey(HEADER_NAME)
        assertThat(result[HEADER_NAME]).isNull()
    }

    private class TestController {

        @Endpoint("test", method = HttpMethod.GET)
        fun endpoint(@Header(HEADER_NAME) authorizationHeader: String?): HttpResponse<String> {
            return HttpResponse.ok(authorizationHeader)
        }
    }
}
