package core

import com.lamblin.core.ControllerRegistry
import com.lamblin.core.exception.MissingControllerAnnotationException
import com.lamblin.core.model.annotation.Controller
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ControllerRegistryTest {

    @Test
    fun `should find controller for class when controller instance present`() {
        val controller = ValidController()
        val controllerRegistry = ControllerRegistry(setOf(controller))

        val controllerFound = controllerRegistry.controllerForClass(ValidController::class.java)

        assertThat(controllerFound).isEqualTo(controller)
    }

    @Test
    fun `should return null value when controller instance not found`() {
        val controllerRegistry = ControllerRegistry(setOf())

        val controllerFound = controllerRegistry.controllerForClass(ValidController::class.java)

        assertThat(controllerFound).isNull()
    }

    @Test
    fun `should return controller classes`() {
        val controllerRegistry = ControllerRegistry(setOf(ValidController()))

        val controllerClasses = controllerRegistry.controllerClasses()

        assertThat(controllerClasses).hasSize(1)
        assertThat(controllerClasses).contains(ValidController::class.java)
    }

    @Test
    fun `should throw MissingControllerAnnotation annotation if controller annotation missing`() {
        assertThrows<MissingControllerAnnotationException> {
            ControllerRegistry(setOf(MissingAnnotationController()))
        }
    }

    @Controller
    class ValidController

    class MissingAnnotationController
}
