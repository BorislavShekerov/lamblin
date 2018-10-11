/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.test.config

/** Defines the details required to configure the test runner. */
data class TestRunnerConfig(val port: Int, val controllers: Set<Any>)
