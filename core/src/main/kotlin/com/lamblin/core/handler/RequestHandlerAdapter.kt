package com.lamblin.core.handler

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.fasterxml.jackson.core.JsonParseException
import com.lamblin.core.OBJECT_MAPPER
import com.lamblin.core.exception.EventDeserializationException
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import java.io.InputStream
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
            input: InputStream,
            output: OutputStream,
            httpMethodToHandlers: Map<HttpMethod, Set<HandlerMethod>>) {

        try {
            val request = OBJECT_MAPPER.readValue(input, APIGatewayProxyRequestEvent::class.java)

            val response = requestHandler.handle(request, httpMethodToHandlers)

            output.write(OBJECT_MAPPER.writeValueAsBytes(response))
        } catch (e: JsonParseException) {
            throw EventDeserializationException(
                    "Unable to deserialize event into APIGatewayProxyRequestEvent. " +
                            "Please make sure lambda was triggered by an API Gateway event", e)
        }
    }

}
