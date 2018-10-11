/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package core.handler

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.EndpointInvoker
import com.lamblin.core.handler.RequestHandler
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.StatusCode
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class RequestHandlerTest {

    private val endpointInvoker: EndpointInvoker = mockk()
    private val requestHandler = RequestHandler(endpointInvoker)

    private val apiGatewayProxyRequestEvent: APIGatewayProxyRequestEvent = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        clearMocks(endpointInvoker, apiGatewayProxyRequestEvent)

        every { apiGatewayProxyRequestEvent.httpMethod } returns "GET"
    }

    @Test
    fun `should return 404 when no handler methods handling the request HTTP method`() {
        val response = requestHandler.handle(apiGatewayProxyRequestEvent, mapOf())

        assertThat(response.statusCode).isEqualTo(404)
    }

    @Test
    fun `should return 404 when no handler method matches request path`() {
        val requestPath = "path"
        every { apiGatewayProxyRequestEvent.path } returns requestPath
        every { apiGatewayProxyRequestEvent.queryStringParameters } returns mapOf()

        val handlerMethodMock: HandlerMethod = mockk(relaxed = true)
        every { handlerMethodMock.matches(requestPath, mapOf()) } returns false

        val response = requestHandler.handle(
            apiGatewayProxyRequestEvent,
            mapOf(HttpMethod.GET to setOf(handlerMethodMock))
        )

        assertThat(response.statusCode).isEqualTo(404)
    }

    @Test
    fun `should return 500 when exception occurs during handler execution`() {
        val requestPath = "path"
        every { apiGatewayProxyRequestEvent.path } returns requestPath
        every { apiGatewayProxyRequestEvent.queryStringParameters } returns mapOf()

        val handlerMethodMock: HandlerMethod = mockk(relaxed = true)
        every { handlerMethodMock.matches(requestPath, mapOf()) } returns true
        every {
            endpointInvoker.invoke(
                handlerMethodMock,
                apiGatewayProxyRequestEvent)
        } throws RuntimeException("Error executing")

        val response = requestHandler.handle(
            apiGatewayProxyRequestEvent,
            mapOf(HttpMethod.GET to setOf(handlerMethodMock)))

        assertThat(response.statusCode).isEqualTo(500)
    }

    @Test
    fun `should return 200 when request handled properly`() {
        val requestPath = "path"
        every { apiGatewayProxyRequestEvent.path } returns requestPath
        every { apiGatewayProxyRequestEvent.queryStringParameters } returns mapOf()

        val handlerMethodMock: HandlerMethod = mockk(relaxed = true)
        every { handlerMethodMock.matches(requestPath, mapOf()) } returns true
        every { endpointInvoker.invoke(handlerMethodMock, apiGatewayProxyRequestEvent) } returns HttpResponse(
            body = Result(true))

        val response = requestHandler.handle(
            apiGatewayProxyRequestEvent,
            mapOf(HttpMethod.GET to setOf(handlerMethodMock))
        )

        assertThat(response.statusCode).isEqualTo(200)
        assertThat(response.headers).isEqualTo(mapOf("Content-Type" to "application/json"))
        assertThat(response.body).isEqualTo("""{"success":true}""")
    }

    @Test
    fun `should return status code returned handler`() {
        val requestPath = "path"
        every { apiGatewayProxyRequestEvent.path } returns requestPath
        every { apiGatewayProxyRequestEvent.queryStringParameters } returns mapOf()

        val handlerMethodMock: HandlerMethod = mockk(relaxed = true)
        every { handlerMethodMock.matches(requestPath, mapOf()) } returns true
        every { endpointInvoker.invoke(handlerMethodMock, apiGatewayProxyRequestEvent) } returns HttpResponse<Any>(
            statusCode = StatusCode.UNAUTHORIZED)

        val response = requestHandler.handle(
            apiGatewayProxyRequestEvent,
            mapOf(HttpMethod.GET to setOf(handlerMethodMock)))

        assertThat(response.statusCode).isEqualTo(401)
        assertThat(response.headers).isEmpty()
        assertThat(response.body).isBlank()
    }

    @Test
    fun `should return headers returned handler`() {
        val requestPath = "path"
        every { apiGatewayProxyRequestEvent.path } returns requestPath
        every { apiGatewayProxyRequestEvent.queryStringParameters } returns mapOf()

        val handlerMethodMock: HandlerMethod = mockk(relaxed = true)
        every { handlerMethodMock.matches(requestPath, mapOf()) } returns true
        val headers = mapOf("Authorization" to "Token")
        every { endpointInvoker.invoke(handlerMethodMock, apiGatewayProxyRequestEvent) } returns HttpResponse<Any>(
            statusCode = StatusCode.FORBIDDEN,
            headers = headers)

        val response = requestHandler.handle(
            apiGatewayProxyRequestEvent,
            mapOf(HttpMethod.GET to setOf(handlerMethodMock)))

        assertThat(response.statusCode).isEqualTo(403)
        assertThat(response.headers).isEqualTo(headers)
        assertThat(response.body).isBlank()
    }

    data class Result(val success: Boolean = true)
}
