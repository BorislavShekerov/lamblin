package core

import com.lamblin.core.DefaultPluginRegistry
import com.lamblin.plugin.core.ExecutableLamblinPlugin
import com.lamblin.plugin.core.model.PluginExecutionResult
import com.lamblin.plugin.core.model.PluginType
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultPluginRegistryTest {

    private var mockPlugin: ExecutableLamblinPlugin = mockk()

    @BeforeEach
    fun setUp() {
        every { mockPlugin.getPluginType() } returns PluginType.WARMUP
    }

    @AfterEach
    fun tearDown() {
        clearMocks(mockPlugin)
    }

    @Test
    fun `should find registered plugin`() {
        DefaultPluginRegistry.registerPlugin(mockPlugin)

        assertThat(DefaultPluginRegistry.isPluginRegistered(PluginType.WARMUP)).isTrue()
    }

    @Test
    fun `should execute registered plugin`() {
        val input: Map<String, Any> = mapOf()

        every { mockPlugin.execute(input) } returns PluginExecutionResult.SUCCESS

        DefaultPluginRegistry.registerPlugin(mockPlugin)

        assertThat(DefaultPluginRegistry.executePlugin(PluginType.WARMUP, input)).isEqualTo(PluginExecutionResult.SUCCESS)
    }
}
