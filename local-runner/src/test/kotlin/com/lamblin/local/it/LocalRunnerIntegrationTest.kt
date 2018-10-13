package com.lamblin.local.it

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.lamblin.local.runner.LocalRunner
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Test

const val PORT = 7070

class LocalRunnerIntegrationTest {

    private val runner = LocalRunner.createRunner(PORT, 1, Controller())
    private val objectMapper = ObjectMapper().apply {
        registerModule(KotlinModule())
    }

    init {
        runner.run()
    }

    @AfterAll
    fun stopRunner() {
        runner.stop()
    }

    @Test
    fun `should handler request valid request`() {
        val clientApi = ClientApi.create("http://localhost:$PORT", objectMapper)

        var response = clientApi.test().execute()

        assertThat(response.isSuccessful).isTrue()
        assertThat(response.body()).isEqualTo(FooResponse(RESPONSE_CONTENT))
    }
}
