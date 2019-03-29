/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.handler

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.fasterxml.jackson.databind.JsonMappingException
import com.lamblin.core.jsonMapperRegistry
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import org.slf4j.LoggerFactory
import java.io.InputStream
import java.io.OutputStream

private val LOGGER = LoggerFactory.getLogger(RequestHandlerAdapter::class.java)

/**
 * Defines an adapter on top of [RequestHandler] which deals with raw input and out streams.
 */
internal class RequestHandlerAdapter internal constructor(private val requestHandler: RequestHandler) {

    /**
     * Handles deserialising the raw request input into an [APIGatewayProxyRequestEvent]
     * and then writing the result [ApiGatewayProxyResponseEvent] to the output
     */
    fun handlerRequest(
        eventInputStream: InputStream,
        output: OutputStream,
        httpMethodToHandlers: Map<HttpMethod, Set<HandlerMethod>>) {

        val request = transformRequestContentsToAwsProxyRequest(eventInputStream)

        request?.let {
            val response = requestHandler.handle(request, httpMethodToHandlers)
            output.write(jsonMapperRegistry.jsonMapper.writeValueAsBytes(response))
        }
    }

    private fun transformRequestContentsToAwsProxyRequest(eventInputStream: InputStream) =
        try {
            jsonMapperRegistry.jsonMapper.readValue(eventInputStream, APIGatewayProxyRequestEvent::class.java)
        } catch (e: JsonMappingException) {
            LOGGER.warn("Event received not a valid API Gateway proxy request.")
            null
        }

}
