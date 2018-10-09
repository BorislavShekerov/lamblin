package com.lamblin.test.common

import com.lamblin.test.config.LamblinTestConfig
import com.lamblin.test.config.annotation.DEFAULT_SERVER_PORT
import com.lamblin.test.config.annotation.LamblinTestRunnerConfig
import com.lamblin.test.exception.IllegalTestConfigClassException
import com.lamblin.test.exception.TestRunnerConfigAnnotationMissingException
import com.lamblin.test.junit4.JUnit4LamblinTestRunner
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.junit.runner.RunWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DefaultTestRunnerConfigExtractorTest {

    @Test
    fun `should throw TestRunnerConfigAnnotationMissingException when LamblinTestRunnerConfig missing`() {
        assertThrows<TestRunnerConfigAnnotationMissingException> {
            DefaultTestRunnerConfigExtractor.extractConfigFromTestClass(TestClassMissingAnnotation::class.java)
        }
    }

    @Test
    fun `should throw IllegalTestConfigClassException when LamblinTestRunnerConfig present but config class not LamblinTestConfig`() {
        assertThrows<IllegalTestConfigClassException> {
            DefaultTestRunnerConfigExtractor.extractConfigFromTestClass(TestClassWithInvalidConfiguration::class.java)
        }
    }

    @Test
    fun `should return test runner config when test class is properly configured`() {
        val config =
            DefaultTestRunnerConfigExtractor.extractConfigFromTestClass(TestWithValidTestConfiguration::class.java)

        assertThat(config.port).isEqualTo(DEFAULT_SERVER_PORT)
        assertThat(config.controllers).hasSize(1)
        assertThat(config.controllers.first()).isInstanceOf(Controller::class.java)
    }


    class TestClassMissingAnnotation

    @RunWith(JUnit4LamblinTestRunner::class)
    @LamblinTestRunnerConfig(testConfigClass = InvalidTestConfiguration::class)
    class TestClassWithInvalidConfiguration

    class InvalidTestConfiguration

    @RunWith(JUnit4LamblinTestRunner::class)
    @LamblinTestRunnerConfig(testConfigClass = ValidTestConfiguration::class)
    class TestWithValidTestConfiguration

    class ValidTestConfiguration : LamblinTestConfig {

        override fun controllers(): Set<Any> {
            return setOf(Controller())
        }
    }

    class Controller

}
