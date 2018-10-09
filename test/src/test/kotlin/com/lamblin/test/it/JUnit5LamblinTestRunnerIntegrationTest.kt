package com.lamblin.test.it

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.lamblin.test.config.annotation.LamblinTestRunnerConfig
import com.lamblin.test.it.common.CONTROLLER1_RESPONSE_CONTENT
import com.lamblin.test.it.common.CONTROLLER2_RESPONSE_CONTENT
import com.lamblin.test.it.common.Controller1Client
import com.lamblin.test.it.common.Controller2Client
import com.lamblin.test.it.common.TestConfiguration
import com.lamblin.test.junit5.JUnit5LamblinExtension
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(JUnit5LamblinExtension::class)
@LamblinTestRunnerConfig(serverPort = PORT, testConfigClass = TestConfiguration::class)
class JUnit5LamblinTestRunnerIntegrationTest {

    private val objectMapper = ObjectMapper().apply {
        registerModule(KotlinModule())
    }

    private val controller1Client = Controller1Client.create("http://localhost:$PORT", objectMapper)
    private val controller2Client = Controller2Client.create("http://localhost:$PORT", objectMapper)

    @Test
    fun `should have the server started and controller1 endpoint should be accessible`() {
        val response = controller1Client.callEndpoint().execute()

        Assertions.assertThat(response.isSuccessful).isTrue()
        Assertions.assertThat(response.body()?.content).isEqualTo(CONTROLLER1_RESPONSE_CONTENT)
    }

    @Test
    fun `should have the server started and controller2 endpoint should be accessible`() {
        val response = controller2Client.callEndpoint().execute()

        Assertions.assertThat(response.isSuccessful).isTrue()
        Assertions.assertThat(response.body()?.content).isEqualTo(CONTROLLER2_RESPONSE_CONTENT)
    }

}
