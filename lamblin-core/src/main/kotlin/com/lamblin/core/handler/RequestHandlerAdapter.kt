/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.handler

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.fasterxml.jackson.core.JsonParseException
import com.lamblin.core.OBJECT_MAPPER
import com.lamblin.core.exception.EventDeserializationException
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import java.io.OutputStream

/**
 * Defines an adapter on top of [RequestHandler] which deals with raw input and out streams.
 */
internal class RequestHandlerAdapter internal constructor(private val requestHandler: RequestHandler) {

    /**
     * Handles deserialising the raw request input into an [APIGatewayProxyRequestEvent]
     * and then writing the result [ApiGatewayProxyResponseEvent] to the output
     */
    fun handlerRequest(
        requestContents: Map<String, Any>,
        output: OutputStream,
        httpMethodToHandlers: Map<HttpMethod, Set<HandlerMethod>>) {

        val request = transformRequestContentsToAwsProxyRequest(requestContents)

        val response = requestHandler.handle(request, httpMethodToHandlers)

        output.write(OBJECT_MAPPER.writeValueAsBytes(response))
    }

    private fun transformRequestContentsToAwsProxyRequest(requestContents: Map<String, Any>) =
        APIGatewayProxyRequestEvent().apply {
            path = requestContents["path"] as? String
            httpMethod = requestContents["httpMethod"] as? String
            headers = requestContents["headers"] as? Map<String, String>
            queryStringParameters = requestContents["queryStringParameters"] as? Map<String, String>
            body = requestContents["body"] as? String
        }

}
