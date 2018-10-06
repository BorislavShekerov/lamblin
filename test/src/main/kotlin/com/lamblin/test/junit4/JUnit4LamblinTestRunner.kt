package com.lamblin.test.junit4

import com.lamblin.core.FrontController
import org.junit.runners.BlockJUnit4ClassRunner
import org.junit.runners.model.FrameworkMethod
import org.junit.runners.model.Statement
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

private val LOGGER = LoggerFactory.getLogger(LamblinLocalRunner::class.java)

class LamblinLocalRunner constructor(klass: Class<*>): BlockJUnit4ClassRunner(klass) {

    init {
        startServerUp()
    }

    override fun methodInvoker(method: FrameworkMethod, test: Any): Statement {
        System.out.println("invoking: " + method.getName())
        return super.methodInvoker(method, test)
    }

    fun startServerUp() {

    }

}