/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.test.config

/** Used for defining the controllers to be used by the lamblin test runner. */
interface LamblinTestConfig {

    /** Returns the list of controller instances to be used by the lamblin test runner. */
    fun controllers(): Set<Any>

}
