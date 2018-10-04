package com.lamblin.core

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.handler.HandlerMethodFactory
import com.lamblin.core.handler.RequestHandler
import com.lamblin.core.handler.RequestHandlerAdapter
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.annotation.Endpoint
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.io.OutputStream

private val LOGGER = LoggerFactory.getLogger(FrontController::class.java)

/**
 * Defines the controller responsible for delegating the requests to the endpoint controllers.
 */
class FrontController internal constructor(
        private val requestHandler: RequestHandlerAdapter,
        private val handlerMethodFactory: HandlerMethodFactory,
        private val controllerRegistry: ControllerRegistry) {

    private val httpMethodToHandlers: Map<HttpMethod, Set<HandlerMethod>> = createHttpMethodToPathToHandlerMethodMap()

    companion object {

        /** Creates an instance using a list of controller endpoint classes. */
        fun instance(controllers: Set<Any>): FrontController {
            val controllerRegistry = ControllerRegistry(controllers)

            return FrontController(
                    RequestHandlerAdapter(
                            RequestHandler.instance(controllerRegistry = controllerRegistry)),
                    HandlerMethodFactory.default(),
                    controllerRegistry)
        }
    }

    internal fun createHttpMethodToPathToHandlerMethodMap(): Map<HttpMethod, Set<HandlerMethod>> {
        return controllerRegistry.controllerClasses()
                .flatMap { this.createHttpMethodToHandlerMethodMap(it).entries }
                .groupBy ({ it.key }, { it.value })
                .mapValues { it.value.flatMap { it }.toSet() }
    }

    private fun createHttpMethodToHandlerMethodMap(
            controllerClass: Class<out Any>): Map<HttpMethod, Set<HandlerMethod>> {

        LOGGER.debug("Creating handlers for [{}]", controllerClass.canonicalName)

        val controllerEndpoints = setOf(*controllerClass.declaredMethods)
                .filter { it.annotations.any { it is Endpoint } }

        return controllerEndpoints
                .map { handlerMethodFactory.method(it, controllerClass) }
                .groupBy { it.httpMethod }
                .mapValues { it.value.toSet() }
    }

    /**
     * Attempts to handle an [APIGatewayProxyRequestEvent] by first looking for
     * a suitable [HandlerMethod] and then delegating the execution to it.
     */
    fun handlerRequest(input: InputStream, output: OutputStream) {
        requestHandler.handlerRequest(input, output, httpMethodToHandlers)
    }
}


