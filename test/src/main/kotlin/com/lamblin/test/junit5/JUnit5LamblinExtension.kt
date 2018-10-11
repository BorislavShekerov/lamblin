/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.test.junit5

import com.lamblin.local.runner.LocalRunner
import com.lamblin.test.common.TestRunnerConfigExtractor
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext

/** Defines the test runner to be used with JUnit 5. */
class JUnit5LamblinExtension : BeforeAllCallback, AfterAllCallback {

    private val testRunnerConfigExtractor: TestRunnerConfigExtractor = TestRunnerConfigExtractor.default()
    private var localRunner: LocalRunner? = null

    override fun beforeAll(extensionContext: ExtensionContext) {
        localRunner =
                createLocalRunner(
                    extensionContext.testClass.orElseThrow { throw IllegalStateException("Test class not found") })

        localRunner?.run()
    }

    private fun createLocalRunner(testClass: Class<*>): LocalRunner {
        val (port, controllers) = testRunnerConfigExtractor.extractConfigFromTestClass(testClass)

        return LocalRunner.createRunner(port, controllers, 1)
    }

    override fun afterAll(p0: ExtensionContext) {
        localRunner?.stop()
    }

}
