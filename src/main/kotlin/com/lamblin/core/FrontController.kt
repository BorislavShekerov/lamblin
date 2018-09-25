package com.lamblin.core

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.annotation.Endpoint
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

val OBJECT_MAPPER = ObjectMapper().apply { registerModule(JavaTimeModule()) }
private val LOGGER = LoggerFactory.getLogger(FrontController::class.java)

/**
 * Defines the controller responsible for delegating the requests to the endpoint controllers.
 */
class FrontController private constructor(
        private val requestHandler: RequestHandler,
        private val handlerMethodFactory: HandlerMethodFactory,
        private val controllerRegistry: ControllerRegistry) {

    private var httpMethodToHandlers: Map<HttpMethod, Set<HandlerMethod>> = createHttpMethodToPathToHandlerMethodMap()

    companion object {

        /**
         * Creates an instance using a list of controller endpoint classes.
         *
         * @param controllers the controllers
         * @return the front controller instance
         */
        fun instance(controllers: Set<Any>): FrontController {
            val controllerRegistry = ControllerRegistry(setOf(controllers))

            return FrontController(
                    RequestHandler.noAlertingInstance(controllerRegistry = controllerRegistry),
                    HandlerMethodFactory.default(),
                    controllerRegistry)
        }
    }

    // Visible for testing
    internal constructor(
            httpMethodToHandlers: Map<HttpMethod, Set<HandlerMethod>>,
            handlerMethodFactory: HandlerMethodFactory,
            controllerRegistry: ControllerRegistry,
            requestHandler: RequestHandler) : this(requestHandler, handlerMethodFactory, controllerRegistry) {

        this.httpMethodToHandlers = httpMethodToHandlers
    }

    private fun createHttpMethodToPathToHandlerMethodMap(): Map<HttpMethod, Set<HandlerMethod>> {
        val methodToHandlersForEachHandlerMethod = controllerRegistry.controllerClasses()
                .map { this.createHttpMethodToHandlerMethodMap(it) }
                .map { it.entries }
                .flatten()

        return methodToHandlersForEachHandlerMethod
                .asSequence()
                .groupBy({ it.key }, { it.value })
                .map { it.key to it.value.flatMap { it } as Set<HandlerMethod> }
                .toMap()
    }

    // TODO Refactor
    private fun createHttpMethodToHandlerMethodMap(
            controllerClass: KClass<out Any>): Map<HttpMethod, Set<HandlerMethod>> {

        LOGGER.debug("Creating handlers for [{}]", controllerClass.qualifiedName)
        var httpMethodToHandlerMethods: Map<HttpMethod, Set<HandlerMethod>> = mapOf()

        val controllerEndpoints = controllerClass.members
                .filter { it.annotations.any { it is Endpoint } }

        for (controllerEndpoint in controllerEndpoints) {
            LOGGER.debug("Processing method [{}] for [{}]", controllerEndpoint.name, controllerClass.qualifiedName)

            val handlerMethod = handlerMethodFactory.method(controllerEndpoint, controllerClass)

            httpMethodToHandlerMethods = addHandlerMethodToHandlerMethods(httpMethodToHandlerMethods, handlerMethod)
        }

        return httpMethodToHandlerMethods
    }

    private fun addHandlerMethodToHandlerMethods(
            methodToHandlers: Map<HttpMethod, Set<HandlerMethod>>,
            handlerMethod: HandlerMethod
    ): Map<HttpMethod, Set<HandlerMethod>> =

            methodToHandlers + mapOf(handlerMethod.httpMethod to
                                             (methodToHandlers.getOrDefault(handlerMethod.httpMethod, setOf())
                                                     + handlerMethod))

    /**
     * Attempts to handle an [APIGatewayProxyRequestEvent] by first looking for
     * a suitable [HandlerMethod] and then delegating the execution to it.
     */
    fun handlerRequest(request: APIGatewayProxyRequestEvent) = requestHandler.handle(request, httpMethodToHandlers)

}
