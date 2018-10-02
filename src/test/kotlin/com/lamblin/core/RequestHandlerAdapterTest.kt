package com.lamblin.core

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.lamblin.core.exception.EventDeserializationException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayInputStream
import java.io.OutputStream

class RequestHandlerAdapterTest {

    private val requestHandler: RequestHandler = mockk()

    private val requestHandlerAdapter = RequestHandlerAdapter(requestHandler)

    @Test
    fun `should throw EventDeserializationException when event cannot be deserialzied into APIGatewayProxyRequestEvent`() {
        assertThrows<EventDeserializationException> {
            requestHandlerAdapter.handlerRequest(ByteArrayInputStream("test".toByteArray()), mockk(), mapOf())
        }
    }

    @Test
    fun `should write response to output stream`() {
        val request = APIGatewayProxyRequestEvent().apply {
            queryStringParameters = mapOf("query1" to "value1")
        }

        val output: OutputStream = mockk(relaxed = true)
        val response = APIGatewayProxyResponseEvent().apply {
            statusCode = 200
        }

        every { requestHandler.handle(request, mapOf()) } returns response

        requestHandlerAdapter.handlerRequest(
                ByteArrayInputStream(OBJECT_MAPPER.writeValueAsBytes(request)),
                output,
                mapOf())

        verify { output.write(OBJECT_MAPPER.writeValueAsBytes(response)) }
    }
}