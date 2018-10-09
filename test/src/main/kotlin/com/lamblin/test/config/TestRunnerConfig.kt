package com.lamblin.test.config

/** Defines the details required to configure the test runner. */
data class TestRunnerConfig(val port: Int, val controllers: Set<Any>)
