package com.lamblin.local.runner

import com.lamblin.core.model.HttpMethod
import io.javalin.Javalin
import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger(EndpointRegistrator::class.java)

class EndpointRegistrator(
        private val server: Javalin,
        private val frontControllerDelegator: FrontControllerDelegator) {

    internal fun registerEndpoints() {
        frontControllerDelegator.frontController.httpMethodToHandlers.values
                .flatMap { it }
                .forEach {
                    LOGGER.info("Creating handler [{}] [{}]", it.httpMethod, it.path)

                    when (it.httpMethod) {
                        HttpMethod.GET -> server.get(
                                formatPath(it.path)) { frontControllerDelegator.delegateToController(it) }
                        HttpMethod.POST -> server.post(
                                formatPath(it.path)) { frontControllerDelegator.delegateToController(it) }
                        HttpMethod.DELETE -> server.delete(
                                formatPath(it.path)) { frontControllerDelegator.delegateToController(it) }
                        HttpMethod.PUT -> server.put(
                                formatPath(it.path)) { frontControllerDelegator.delegateToController(it) }
                        HttpMethod.PATCH -> server.patch(
                                formatPath(it.path)) { frontControllerDelegator.delegateToController(it) }
                    }
                }
    }

    // Formats the path params from {param} to :param
    private fun formatPath(path: String) =
            path.split("/")
                    .asSequence()
                    .map {
                        if (it.startsWith("{")) ":${it.removeSurrounding("{", "}")}" else it
                    }.joinToString("/")
}
