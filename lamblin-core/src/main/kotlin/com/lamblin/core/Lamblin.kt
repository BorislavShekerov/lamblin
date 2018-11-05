/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.handler.HandlerMethodFactory
import com.lamblin.core.handler.RequestHandler
import com.lamblin.core.handler.RequestHandlerAdapter
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.plugin.core.ExecutableLamblinPlugin
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.io.OutputStream
import java.util.Objects.nonNull
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

private val LOGGER = LoggerFactory.getLogger(Lamblin::class.java)

/**
 * Defines the controller responsible for delegating the requests to the endpoint controllers.
 */
class Lamblin internal constructor(
    private val requestHandler: RequestHandlerAdapter,
    private val handlerMethodFactory: HandlerMethodFactory,
    private val controllerRegistry: ControllerRegistry,
    private val pluginRegistry: PluginRegistry) {

    val httpMethodToHandlers: Map<HttpMethod, Set<HandlerMethod>> = createHttpMethodToPathToHandlerMethodMap()

    companion object {

        /** Creates an frontController using a list of controller endpoint classes. */
        @JvmStatic
        fun frontController(vararg controllers: Any): Lamblin {
            val controllerRegistry = ControllerRegistry(controllers.toSet())

            return Lamblin(
                RequestHandlerAdapter(
                    RequestHandler.instance(controllerRegistry)),
                HandlerMethodFactory.default(),
                controllerRegistry,
                DefaultPluginRegistry)
        }
    }

    /**
     * Attempts to handle an [APIGatewayProxyRequestEvent] by first looking for
     * a suitable [HandlerMethod] and then delegating the execution to it.
     */
    fun handlerRequest(input: InputStream, output: OutputStream) {
        requestHandler.handlerRequest(input, output, httpMethodToHandlers)
    }

    fun registerPlugin(plugin: ExecutableLamblinPlugin) {
        pluginRegistry.registerPlugin(plugin)
    }

    internal fun createHttpMethodToPathToHandlerMethodMap(): Map<HttpMethod, Set<HandlerMethod>> {
        return controllerRegistry.controllerClasses()
            .flatMap { this.createHttpMethodToHandlerMethodMap(it).entries }
            .groupBy({ it.key }, { it.value })
            .mapValues { it.value.flatMap { handlerMethods -> handlerMethods }.toSet() }
    }

    private fun createHttpMethodToHandlerMethodMap(
        controllerClass: KClass<out Any>
    ): Map<HttpMethod, Set<HandlerMethod>> {

        LOGGER.debug("Creating handlers for [{}]", controllerClass.simpleName)

        val controllerEndpoints = controllerClass.members
            .filter { nonNull(it.findAnnotation<Endpoint>()) }

        return controllerEndpoints.asSequence()
            .map { handlerMethodFactory.method(it as KCallable<HttpResponse<*>>, controllerClass) }
            .groupBy { it.httpMethod }
            .mapValues { it.value.toSet() }
    }

}
