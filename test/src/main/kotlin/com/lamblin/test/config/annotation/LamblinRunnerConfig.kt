package com.lamblin.test.config.annotation

import kotlin.reflect.KClass

/**
 * Defines the mechanism for defining test runner configuration.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LamblinRunnerConfig(val serverPort: Int, val controllers: KClass<*>)
