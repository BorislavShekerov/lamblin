/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.test.junit4

import com.lamblin.local.runner.LocalRunner
import com.lamblin.test.common.TestRunnerConfigExtractor
import org.junit.Test
import org.junit.runner.Description
import org.junit.runner.Runner
import org.junit.runner.notification.Failure
import org.junit.runner.notification.RunNotifier
import org.slf4j.LoggerFactory
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


private val LOGGER = LoggerFactory.getLogger(JUnit4LamblinTestRunner::class.java)

class JUnit4LamblinTestRunner constructor(private val testClass: Class<*>) : Runner() {

    private val testRunnerConfigExtractor = TestRunnerConfigExtractor.default()
    private val localRunner: LocalRunner

    init {
        localRunner = createLocalRunner()
    }

    override fun getDescription() = Description.createSuiteDescription(testClass)

    override fun run(notifier: RunNotifier) {
        Runner.run(localRunner, notifier, testClass)
    }

    private fun createLocalRunner(): LocalRunner {
        val (port, controllers) = testRunnerConfigExtractor.extractConfigFromTestClass(testClass)

        return LocalRunner.createRunner(port, controllers, 1)
    }

    object Runner {

        fun run(localRunner: LocalRunner, notifier: RunNotifier, testClass: Class<*>) {
            LOGGER.info("Starting Lamblin local runner")
            localRunner.run()

            try {
                val testObject = testClass.newInstance()
                testClass.methods
                    .filter { it.isAnnotationPresent(Test::class.java) }
                    .forEach { executeTest(notifier, it, testObject, testClass) }
            } finally {
                localRunner.stop()
                LOGGER.info("Starting Lamblin local runner")
            }
        }

        private fun executeTest(
            notifier: RunNotifier,
            testMethod: Method,
            testObject: Any?,
            testClass: Class<*>
        ) {

            val testDescription = Description.createTestDescription(testClass, testMethod.name)

            try {
                notifier.fireTestStarted(testDescription)
                testMethod.invoke(testObject)
                notifier.fireTestFinished(testDescription)
            } catch (e: InvocationTargetException) {
                LOGGER.error("Error occurred while executing test ${testMethod.name}")
                notifier.fireTestFailure(Failure(testDescription, e))
            }
        }
    }
}
