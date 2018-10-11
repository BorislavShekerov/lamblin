package com.lamblin.plugin.warmup

import com.lamblin.plugin.core.model.PluginExecutionResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WarmupLambdaPluginTest {

    private val warmupLambdaPlugin = WarmupLambdaPlugin()

    @Test
    fun `should skip execution if event not warmup`() {
        val result = warmupLambdaPlugin.execute(mapOf())

        assertThat(result).isEqualTo(PluginExecutionResult.SKIPPED)
    }

    @Test
    fun `should return success when event warmup`() {
        val result = warmupLambdaPlugin.execute(mapOf(DEFAULT_WARMUP_EVENT_KEY to DEFAULT_WARMUP_EVENT_KEY))

        assertThat(result).isEqualTo(PluginExecutionResult.SUCCESS)
    }
}
