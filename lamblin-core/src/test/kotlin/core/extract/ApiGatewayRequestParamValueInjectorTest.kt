package core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.extract.ApiGatewayRequestParamValueInjector
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.PathParam
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ApiGatewayRequestParamValueInjectorTest {

    @Test
    fun `should return empty map if no ApiGatewayRequestParamValue param present in handler method`() {
        val requestEvent = APIGatewayProxyRequestEvent().apply {
            path = "test"
        }

        val pathParam = TestController::class.members
            .find { it.name == "noRequestEventEndpoint" }!!
            .parameters.find { it.name == "uid" }!!

        val result = ApiGatewayRequestParamValueInjector.injectParamValues(
            requestEvent,
            mockk(),
            mapOf("uid" to pathParam))

        assertThat(result).isEmpty()
    }

    @Test
    fun `should inject ApiGatewayRequestParamValue if param present on handler method`() {
        val requestEvent = APIGatewayProxyRequestEvent().apply {
            path = "test"
        }

        val apiGatewayRequestEventParameter = TestController::class.members
            .find { it.name == "requestEventEndpoint" }!!
            .parameters.find { it.name == "apiGatewayProxyRequestEvent" }!!

        val result = ApiGatewayRequestParamValueInjector.injectParamValues(
            requestEvent,
            mockk(),
            mapOf(
                "apiGatewayProxyRequestEvent" to apiGatewayRequestEventParameter))

        assertThat(result).hasSize(1)
        assertThat(result["apiGatewayProxyRequestEvent"]).isEqualTo(requestEvent)
    }

    private class TestController {

        @Endpoint("test", method = HttpMethod.GET)
        fun requestEventEndpoint(apiGatewayProxyRequestEvent: APIGatewayProxyRequestEvent): HttpResponse<String> {
            return HttpResponse.ok(apiGatewayProxyRequestEvent.path)
        }

        @Endpoint("test/{uid}", method = HttpMethod.GET)
        fun noRequestEventEndpoint(@PathParam("uid") uid: String): HttpResponse<String> {
            return HttpResponse.ok(uid)
        }
    }
}
