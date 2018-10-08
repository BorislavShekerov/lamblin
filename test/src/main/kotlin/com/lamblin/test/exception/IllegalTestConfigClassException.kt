package com.lamblin.test.exception

/**
 * Thrown when the test config class passed in [LamblinTestRunnerConfig] is invalid
 * (i.e. not implementing [LamblinTestConfig]).
 */
class IllegalTestConfigClassException(message: String): RuntimeException(message)
