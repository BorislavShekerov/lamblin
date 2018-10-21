package com.lamblin.it

import com.lamblin.core.model.StatusCode
import com.lamblin.it.client.MiscellaneousControllerClient
import com.lamblin.it.controller.MiscellaneousController
import com.lamblin.it.model.*
import com.lamblin.test.config.LamblinTestConfig
import com.lamblin.test.config.annotation.LamblinTestRunnerConfig
import com.lamblin.test.junit5.JUnit5LamblinExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JUnit5LamblinExtension::class)
@LamblinTestRunnerConfig(testConfigClass = MiscellaneousControllerTest.MiscellaneousTestConfiguration::class)
class MiscellaneousControllerTest {

    private val client = MiscellaneousControllerClient

    @Test
    fun `should handler Header injection`() {
        runRequestAndVerifyResponse<ResponseEntity>(
            { client.callHeaderInjectionEndpoint(AUTHORIZATION_HEADER_VALUE) },
                "$HEADER_GET_ENDPOINT-$AUTHORIZATION_HEADER_VALUE")
    }

    @Test
    fun `should return 404 for unknown routes`() {
        runRequestAndVerifyResponse<Void>(
            { client.callUnknownEndpoint() },
            null,
            StatusCode.NOT_FOUND.code)
    }

    @Test
    fun `should handle APIGatewayProxyRequestEvent params`() {
        runRequestAndVerifyResponse<ResponseEntity>(
            { client.callApiGatewayRequestEventEndpoint() },
            "$API_GATEWAY_REQUEST_EVENT_GET_ENDPOINT-$API_GATEWAY_REQUEST_EVENT_GET_ENDPOINT")
    }

    @Test
    fun `should return custom status code if defined in HttpResponse`() {
        runRequestAndVerifyResponse<Void>(
            { client.callCustomStatusCodeEndpoint() },
            null,
            StatusCode.ACCEPTED.code)
    }

    class MiscellaneousTestConfiguration : LamblinTestConfig {

        override fun controllers(): Set<Any> {
            return setOf(MiscellaneousController())
        }
    }
}