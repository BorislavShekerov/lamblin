/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.local.runner

import io.javalin.Javalin
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import com.lamblin.core.Lamblin

internal const val SECONDS_BEFORE_TERMINATING = 1000 * 60 * 60 * 24
private val LOGGER = LoggerFactory.getLogger(LamblinLocalRunner::class.java)

class LamblinLocalRunner internal constructor(
    private val server: Javalin,
    private val endpointRegistrator: EndpointRegistrator,
    private val millisecondsToBlockFor: Long = SECONDS_BEFORE_TERMINATING.toLong()
) {

    private val executor = Executors.newSingleThreadExecutor()

    companion object {

        @JvmStatic
        @JvmOverloads
        fun createRunner(
            port: Int,
            runTimeInMilliseconds: Long = SECONDS_BEFORE_TERMINATING.toLong(),
            vararg controllers: Any
        ): LamblinLocalRunner {

            val server = Javalin.create()
                .port(port)
                .disableStartupBanner()
                .enableCaseSensitiveUrls()
                .enableCorsForOrigin("*")

            val frontControllerDelegator = LamblinDelegator(Lamblin.frontController(*controllers))

            val endpointRegistrator = EndpointRegistrator(server, frontControllerDelegator)

            return LamblinLocalRunner(server, endpointRegistrator, runTimeInMilliseconds)
        }
    }

    fun run() {
        LOGGER.info("Starting Lamblin local runner.")
        server.start()

        endpointRegistrator.registerEndpoints()
        LOGGER.debug("Endpoints Registered")

        executor.submit {
            try {
                TimeUnit.HOURS.sleep(millisecondsToBlockFor)
            } finally {
                server.stop()
            }
        }

        executor.shutdown()
        executor.awaitTermination(millisecondsToBlockFor, TimeUnit.SECONDS)
        LOGGER.debug("Local runner terminated.")
    }

    fun stop() {
        executor.shutdownNow()
    }

}
