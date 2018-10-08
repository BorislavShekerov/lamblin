package com.lamblin.test.common

import com.lamblin.test.config.LamblinTestConfig
import com.lamblin.test.config.TestRunnerConfig
import com.lamblin.test.config.annotation.LamblinTestRunnerConfig
import com.lamblin.test.exception.IllegalTestConfigClassException
import com.lamblin.test.exception.TestRunnerConfigAnnotationMissingException
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

interface TestRunnerConfigExtractor {
    fun extractConfigFromTestClass(testClass: Class<*>): TestRunnerConfig

    companion object {
        fun default() = DefaultTestRunnerConfigExtractor
    }
}

object DefaultTestRunnerConfigExtractor : TestRunnerConfigExtractor {

    override fun extractConfigFromTestClass(testClass: Class<*>): TestRunnerConfig {
        val testRunnerConfig = testClass.annotations
                .find { it is LamblinTestRunnerConfig }
                as? LamblinTestRunnerConfig
                ?: throw TestRunnerConfigAnnotationMissingException(
                        "LamblinTestRunnerConfig annotation missing on class ${testClass.name}")

        return TestRunnerConfig(
                testRunnerConfig.serverPort,
                getControllerInstances(testRunnerConfig.testConfigClass))
    }

    private fun getControllerInstances(configClass: KClass<*>): Set<Any> {
        val configClassInstance = configClass.createInstance()
                as? LamblinTestConfig
                ?: throw IllegalTestConfigClassException(
                        "Config class ${configClass.qualifiedName} should implement LamblinTestConfig")

        return configClassInstance.controllers()
    }
}
