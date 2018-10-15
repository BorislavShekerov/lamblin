package com.lamblin.local.runner

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.lamblin.core.Lamblin
import com.lamblin.core.model.StatusCode
import io.javalin.Context
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.lang.RuntimeException

class LamblinDelegatorTest {

    private val lamblin: Lamblin = mockk(relaxed = true)
    private val objectMapper: ObjectMapper = mockk(relaxed = true)
    private val frontControllerDelegator = LamblinDelegator(lamblin, objectMapper)

    private val requestMethod = "GET"
    private val requestPath = "path"
    private val requestQueryParamMap = mapOf("query1" to listOf("param1"))
    private val requestBody = """{ "content": "value" }"""
    private val requestHeaderMap = mapOf("header1" to "header2")

    private val responseStatus = 200
    private val responseBody = """{ "content": "test"} """
    private val responseHeaders = mapOf("header1" to "header1Val")

    @Test
    fun `should add response to context`() {
        val context: Context = mockk(relaxed = true)

        every { context.method() } returns requestMethod
        every { context.path() } returns requestPath
        every { context.queryParamMap() } returns requestQueryParamMap
        every { context.body() } returns requestBody
        every { context.headerMap() } returns requestHeaderMap

        val responseEvent = createApiGatewayResponseEvent()

        every { objectMapper.readValue("", APIGatewayProxyResponseEvent::class.java) } returns responseEvent

        frontControllerDelegator.delegateToController(context)

        verify { lamblin.handlerRequest(any(), any()) }
        verify { context.status(responseStatus) }
        verify { context.header("header1", "header1Val") }
        verify { context.result(responseBody) }
    }

    @Test
    fun `should return 500 response if exception occurred during execution`() {
        val context: Context = mockk(relaxed = true)

        every { context.method() } returns requestMethod
        every { context.path() } returns requestPath
        every { context.queryParamMap() } returns requestQueryParamMap
        every { context.body() } returns requestBody
        every { context.headerMap() } returns requestHeaderMap

        every { objectMapper.readValue("", APIGatewayProxyResponseEvent::class.java) } throws RuntimeException()

        frontControllerDelegator.delegateToController(context)

        verify { lamblin.handlerRequest(any(), any()) }
        verify { context.status(StatusCode.API_ERROR.code) }
    }

    private fun createApiGatewayResponseEvent() = APIGatewayProxyResponseEvent().apply {
        statusCode = responseStatus
        headers = responseHeaders
        body = responseBody
    }
}
