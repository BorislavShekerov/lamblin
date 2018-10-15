/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.test.exception

/**
 * Thrown when the test config class passed in [LamblinTestRunnerConfig] is invalid
 * (i.e. not implementing [LamblinTestConfig]).
 */
class IllegalTestConfigClassException(message: String): RuntimeException(message)
