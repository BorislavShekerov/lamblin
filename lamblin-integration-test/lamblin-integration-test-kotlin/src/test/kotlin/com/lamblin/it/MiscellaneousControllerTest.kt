package com.lamblin.it

import com.lamblin.core.model.StatusCode
import com.lamblin.it.client.MiscellaneousControllerClient
import com.lamblin.it.controller.MiscellaneousController
import com.lamblin.it.model.AUTHORIZATION_HEADER_VALUE
import com.lamblin.it.model.HEADER_GET_ENDPOINT
import com.lamblin.it.model.ResponseEntity
import com.lamblin.it.model.runRequestAndVerifyResponse
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
    fun shouldHandlePathWithHeaderInjection() {
        runRequestAndVerifyResponse<ResponseEntity>(
            { client.callHeaderInjectionEndpoint(AUTHORIZATION_HEADER_VALUE) },
                "$HEADER_GET_ENDPOINT-$AUTHORIZATION_HEADER_VALUE")
    }

    @Test
    fun shouldReturn404ForUnknownRoutes() {
        runRequestAndVerifyResponse<Void>(
            { client.callUnknownEndpoint() },
            null,
            StatusCode.NOT_FOUND.code)
    }

    @Test
    fun shouldReturnStatusCodeReturnedFromEndpoint() {
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