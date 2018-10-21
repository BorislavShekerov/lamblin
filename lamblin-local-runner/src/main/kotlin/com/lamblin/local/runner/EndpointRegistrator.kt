/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.local.runner

import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import io.javalin.Javalin
import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger(EndpointRegistrator::class.java)

class EndpointRegistrator(
    private val server: Javalin,
    private val lamblinDelegator: LamblinDelegator) {

    internal fun registerEndpoints() {
        lamblinDelegator.lamblin.httpMethodToHandlers
            .mapValues { it.value.distinctBy { it.path } }
            .values
            .flatMap { it }
            .forEach { createRoute(it) }
    }

    private fun createRoute(handlerMethod: HandlerMethod) {
        val (path , httpMethod) = handlerMethod

        LOGGER.info("Creating route [{}] [{}]", httpMethod, path)

        when (httpMethod) {
            HttpMethod.GET -> server.get(formatPath(path)) { lamblinDelegator.delegateToController(it) }
            HttpMethod.POST -> server.post(formatPath(path)) { lamblinDelegator.delegateToController(it) }
            HttpMethod.DELETE -> server.delete(formatPath(path)) { lamblinDelegator.delegateToController(it) }
            HttpMethod.PUT -> server.put(formatPath(path)) { lamblinDelegator.delegateToController(it) }
            HttpMethod.PATCH -> server.patch(formatPath(path)) { lamblinDelegator.delegateToController(it) }
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
