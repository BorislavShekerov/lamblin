/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.test.junit5

import com.lamblin.local.runner.LamblinLocalRunner
import com.lamblin.test.common.TestRunnerConfigExtractor
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext

/** Defines the test runner to be used with JUnit 5. */
class JUnit5LamblinExtension : BeforeAllCallback, AfterAllCallback {

    private val testRunnerConfigExtractor: TestRunnerConfigExtractor = TestRunnerConfigExtractor.default()
    private var localRunner: LamblinLocalRunner? = null

    override fun beforeAll(extensionContext: ExtensionContext) {
        localRunner =
                createLocalRunner(
                    extensionContext.testClass.orElseThrow { throw IllegalStateException("Test class not found") })

        localRunner?.run()
    }

    private fun createLocalRunner(testClass: Class<*>): LamblinLocalRunner {
        val (port, controllers) = testRunnerConfigExtractor.extractConfigFromTestClass(testClass)

        return LamblinLocalRunner.createRunner(
            port,
            runTimeInMilliseconds = 1,
            controllers = *controllers.toTypedArray())
    }

    override fun afterAll(p0: ExtensionContext) {
        localRunner?.stop()
    }

}
