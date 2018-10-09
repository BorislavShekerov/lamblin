package com.lamblin.test.exception

import java.lang.RuntimeException

/** Test thrown when the Test class does not have a [LamblinTestRunnerConfig] annotation. */
class TestRunnerConfigAnnotationMissingException(message: String): RuntimeException(message)
