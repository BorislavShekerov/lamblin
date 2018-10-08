package com.lamblin.local.runner

import com.lamblin.core.FrontController
import io.javalin.Javalin
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

internal const val SECONDS_BEFORE_TERMINATING = 60 * 60
private val LOGGER = LoggerFactory.getLogger(LocalRunner::class.java)

class LocalRunner internal constructor(
        private val server: Javalin,
        private val endpointRegistrator: EndpointRegistrator,
        private val millisecondsToBlockFor: Long = SECONDS_BEFORE_TERMINATING.toLong()) {

    private val executor = Executors.newSingleThreadExecutor()

    companion object {

        @JvmStatic
        @JvmOverloads
        fun createRunner(
                port: Int,
                controllers: Set<Any>,
                runTimeInMilliseconds: Long = SECONDS_BEFORE_TERMINATING.toLong()
        ): LocalRunner {

            val server = Javalin.create().port(port)
            val frontControllerDelegator = FrontControllerDelegator(FrontController.instance(controllers))

            val endpointRegistrator = EndpointRegistrator(server, frontControllerDelegator)

            return LocalRunner(server, endpointRegistrator, runTimeInMilliseconds)
        }
    }

    fun run() {
        server.start()
        endpointRegistrator.registerEndpoints()

        executor.submit {
            try {
                TimeUnit.HOURS.sleep(millisecondsToBlockFor)
            } finally {
                server.stop()
            }
        }

        executor.shutdown()
        executor.awaitTermination(millisecondsToBlockFor, TimeUnit.SECONDS)
    }

    fun stop() {
        executor.shutdownNow()
    }

}
