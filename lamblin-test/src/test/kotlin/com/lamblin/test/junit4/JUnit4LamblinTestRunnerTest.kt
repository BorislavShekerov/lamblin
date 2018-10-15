package com.lamblin.test.junit4

import com.lamblin.local.runner.LamblinLocalRunner
import com.lamblin.test.config.LamblinTestConfig
import com.lamblin.test.config.annotation.LamblinTestRunnerConfig
import com.lamblin.test.it.PORT
import io.mockk.mockk
import io.mockk.verifySequence
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JUnit4LamblinTestRunnerTest {

    private val localRunner: LamblinLocalRunner = mockk(relaxed = true)
    private val notifier: RunNotifier = mockk(relaxed = true)

    @org.junit.jupiter.api.Test
    fun `should create LocalRunner when instance created`() {
        val testRunner = JUnit4LamblinTestRunner(TestClass::class.java)

        assertThat(testRunner.localRunner).isNotNull
        assertThat(testRunner.testRunnerConfigExtractor).isNotNull
    }

    @org.junit.jupiter.api.Test
    fun `should create suite description`() {
        val testRunner = JUnit4LamblinTestRunner(TestClass::class.java)
        val description = testRunner.description

        assertThat(description).isEqualTo(Description.createSuiteDescription(TestClass::class.java))
    }

    @org.junit.jupiter.api.Test
    fun `should run local runner before test and stop after tests are done`() {
        JUnit4LamblinTestRunner.Runner.run(localRunner, notifier, TestClass::class.java)

        val description =
            Description.createTestDescription(TestClass::class.java, TestClass::class.java.methods[0].name)

        verifySequence {
            localRunner.run()
            notifier.fireTestStarted(description)
            notifier.fireTestFinished(description)
            localRunner.stop()
        }
    }
}

@LamblinTestRunnerConfig(serverPort = PORT, testConfigClass = TestConfiguration::class)
class TestClass {

    @Test
    fun dummyTest() {
    }
}

class TestConfiguration: LamblinTestConfig {
    override fun controllers() = setOf<Any>()

}
