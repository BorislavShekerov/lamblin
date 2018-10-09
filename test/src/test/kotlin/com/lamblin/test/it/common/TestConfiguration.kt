package com.lamblin.test.it.common

import com.lamblin.test.config.LamblinTestConfig

class TestConfiguration : LamblinTestConfig {

    override fun controllers(): Set<Any> {
        return setOf(
            Controller1(),
            Controller2())
    }

}
