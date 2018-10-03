package core

import com.lamblin.core.ControllerRegistry
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ControllerRegistryTest {

    @Test
    fun `should find controller for class when controller instance present`() {
        val controller = ControllerRegistryTest()
        val controllerRegistry = ControllerRegistry(setOf(controller))

        val controllerFound = controllerRegistry.controllerForClass(ControllerRegistryTest::class.java)

        assertThat(controllerFound).isEqualTo(controller)
    }

    @Test
    fun `should return null value when controller instance not found`() {
        val controllerRegistry = ControllerRegistry(setOf())

        val controllerFound = controllerRegistry.controllerForClass(ControllerRegistryTest::class.java)

        assertThat(controllerFound).isNull()
    }

    @Test
    fun `should return controller classes`() {
        val controllerRegistry = ControllerRegistry(setOf(ControllerRegistryTest()))

        val controllerClasses = controllerRegistry.controllerClasses()

        assertThat(controllerClasses).hasSize(1)
        assertThat(controllerClasses).contains(ControllerRegistryTest::class.java)
    }
}
