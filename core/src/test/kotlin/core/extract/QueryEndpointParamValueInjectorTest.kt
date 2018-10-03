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

class QueryEndpointParamValueInjectorTest {

    @Test
    fun `should return the value for each query param`() {
        val request: APIGatewayProxyRequestEvent = mockk(relaxed = true)
        every { request.queryStringParameters } returns mapOf("query1" to "value1")

        val result = QueryEndpointParamValueInjector.injectParamValues(
                request,
                mockk(),
                mapOf("query1" to TestController::class.java.declaredMethods[0].parameters[0]))

        assertThat(result).isEqualTo(mapOf("query1" to "value1"))
    }

    private class TestController {

        @Endpoint("test", method = HttpMethod.GET)
        fun endpoint(@QueryParam("query1") queryParam: String): HttpResponse<String> {
            return HttpResponse.ok(queryParam)
        }
    }
}
