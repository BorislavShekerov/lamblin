package com.lamblin.local.runner

import com.lamblin.core.Lamblin
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import io.javalin.Javalin
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class EndpointRegistratorTest {

    private val testPath = "testPath"
    private val server: Javalin = mockk(relaxed = true)
    private val lamblin: Lamblin = mockk(relaxed = true)
    private val lamblinDelegator: LamblinDelegator = mockk(relaxed = true)

    private val endpointRegistrator = EndpointRegistrator(server, lamblinDelegator)

    @BeforeEach
    fun setUp() {
        every { lamblinDelegator.lamblin }.returns(lamblin)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(lamblin, lamblinDelegator, server)
    }

    @Test
    fun `should register GET handler method`() {
        setUpForMethodAndRegister(HttpMethod.GET)

        verify { server.get(testPath, any()) }
    }

    @Test
    fun `should register POST handler method`() {
        setUpForMethodAndRegister(HttpMethod.POST)

        verify { server.post(testPath, any()) }
    }

    @Test
    fun `should register DELETE handler method`() {
        setUpForMethodAndRegister(HttpMethod.DELETE)

        verify { server.delete(testPath, any()) }
    }

    @Test
    fun `should register PUT handler method`() {
        setUpForMethodAndRegister(HttpMethod.PUT)

        verify { server.put(testPath, any()) }
    }

    @Test
    fun `should register PATCH handler method`() {
        setUpForMethodAndRegister(HttpMethod.PATCH)

        verify { server.patch(testPath, any()) }
    }

    @Test
    fun `should format path params before registering endpoint`() {
        val getHandlerMethod: HandlerMethod = mockk(relaxed = true)

        every { lamblin.httpMethodToHandlers } returns mapOf(HttpMethod.GET to setOf(getHandlerMethod))
        every { getHandlerMethod.httpMethod } returns HttpMethod.GET
        val testPathWithPathParam = "/foo/{param}/bar"
        every { getHandlerMethod.path } returns testPathWithPathParam

        endpointRegistrator.registerEndpoints()

        verify { server.get("/foo/:param/bar", any()) }
    }

    private fun setUpForMethodAndRegister(httpMethod: HttpMethod) {
        val getHandlerMethod: HandlerMethod = mockk(relaxed = true)

        every { lamblin.httpMethodToHandlers } returns mapOf(httpMethod to setOf(getHandlerMethod))
        every { getHandlerMethod.httpMethod } returns httpMethod
        val testPath = "testPath"
        every { getHandlerMethod.path } returns testPath

        endpointRegistrator.registerEndpoints()
    }
}
