/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.test.exception

import java.lang.RuntimeException

/** Test thrown when the Test class does not have a [LamblinTestRunnerConfig] annotation. */
class TestRunnerConfigAnnotationMissingException(message: String): RuntimeException(message)
