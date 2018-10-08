package com.lamblin.test.junit4

import com.lamblin.local.runner.LocalRunner
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import org.junit.runner.Description
import org.junit.runner.notification.RunNotifier

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JUnit4LamblinTestRunnerTest {

    private val localRunner: LocalRunner = mockk(relaxed = true)
    private val notifier: RunNotifier = mockk(relaxed = true)

    @org.junit.jupiter.api.Test
    fun `should run local runner before test and stop after tests are done`() {
        JUnit4LamblinTestRunner.Runner.run(localRunner, notifier, TestClass::class.java)

        val description = Description.createTestDescription(TestClass::class.java, TestClass::class.java.methods[0].name)

        verifySequence {
            localRunner.run()
            notifier.fireTestStarted(description)
            notifier.fireTestFinished(description)
            localRunner.stop()
        }
    }

    class TestClass {

        @Test
        fun dummyTest() {
        }
    }
}
