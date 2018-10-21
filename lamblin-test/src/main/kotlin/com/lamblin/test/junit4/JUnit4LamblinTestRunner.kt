/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.test.junit4

import com.lamblin.local.runner.LamblinLocalRunner
import com.lamblin.test.common.TestRunnerConfigExtractor
import org.junit.runners.BlockJUnit4ClassRunner

class JUnit4LamblinTestRunner constructor(private val testClass: Class<*>) : BlockJUnit4ClassRunner(testClass) {

    internal val testRunnerConfigExtractor = TestRunnerConfigExtractor.default()
    internal val localRunner: LamblinLocalRunner

    init {
        localRunner = createLocalRunner()
        localRunner.run()
    }

    private fun createLocalRunner(): LamblinLocalRunner {
        val (port, controllers) = testRunnerConfigExtractor.extractConfigFromTestClass(testClass)

        return LamblinLocalRunner.createRunner(port, 1, *controllers.toTypedArray())
    }

}
