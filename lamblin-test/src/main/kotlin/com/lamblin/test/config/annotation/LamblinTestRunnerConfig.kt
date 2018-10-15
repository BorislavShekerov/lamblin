package com.lamblin.test.config.annotation

import kotlin.reflect.KClass

const val DEFAULT_SERVER_PORT = 8080

/**
 * Defines the mechanism for defining test runner configuration.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LamblinTestRunnerConfig(val serverPort: Int = DEFAULT_SERVER_PORT, val testConfigClass: KClass<*>)
