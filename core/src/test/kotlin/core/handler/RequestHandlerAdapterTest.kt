package core.handler

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.lamblin.core.OBJECT_MAPPER
import com.lamblin.core.handler.RequestHandler
import com.lamblin.core.handler.RequestHandlerAdapter
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.OutputStream

class RequestHandlerAdapterTest {

    private val requestHandler: RequestHandler = mockk()

    private val requestHandlerAdapter = RequestHandlerAdapter(requestHandler)

    @BeforeEach
    fun setUp() {
        clearMocks(requestHandler)
    }

    @Test
    fun `should write response to output stream`() {
        val queryParams =  mapOf("query1" to "value1")

        val request = APIGatewayProxyRequestEvent().apply {
            queryStringParameters = queryParams
        }

        val output: OutputStream = mockk(relaxed = true)
        val response = APIGatewayProxyResponseEvent().apply {
            statusCode = 200
        }

        every { requestHandler.handle(request, mapOf()) } returns response

        requestHandlerAdapter.handlerRequest(
            mapOf("queryStringParameters" to queryParams),
            output,
            mapOf())

        verify { output.write(OBJECT_MAPPER.writeValueAsBytes(response)) }
    }
}
