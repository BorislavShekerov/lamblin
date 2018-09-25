package com.lamblin.core

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.KCallable

class EndpointInvokerClassTest {

    private val method: KCallable<HttpResponse<String>> = mockk(relaxed = true)

    private val handlerMethod = HandlerMethod(
            "/path",
            HttpMethod.GET,
            method = method,
            controllerClass = EndpointInvokerClassTest::class)
    private val requestToParamValueMapper: RequestToParamValueMapper = mockk(relaxed = true)
    private val controllerRegistry: ControllerRegistry = mockk(relaxed = true)

    private val endpointInvoker = EndpointInvoker(requestToParamValueMapper, controllerRegistry)

    @Test
    fun `should throw IllegalStateException if controller for class not found`() {
        every { controllerRegistry.controllerForClass(EndpointInvokerClassTest::class) } returns null

        assertThrows<IllegalStateException> { endpointInvoker.invoke(handlerMethod, APIGatewayProxyRequestEvent()) }
    }

    @Test
    fun `should call method with no parameters if the method does not have any parameters`() {
        every { method.parameters } returns listOf()
        every { controllerRegistry.controllerForClass(EndpointInvokerClassTest::class) } returns EndpointInvokerClassTest()
        every { method.call() } returns HttpResponse()

        endpointInvoker.invoke(handlerMethod, APIGatewayProxyRequestEvent())

        verify { method.call() }
    }

    @Test
    fun `should call method with required parameters`() {

        every { controllerRegistry.controllerForClass(EndpointInvokerClassTest::class) } returns EndpointInvokerClassTest()
        every { method.parameters } returns listOf(mockk())

        val request = APIGatewayProxyRequestEvent()
        val argument = "arg"
        every { requestToParamValueMapper.mapRequestParamsToValues(request, handlerMethod) } returns arrayOf(argument)
        every { method.call(argument) } returns HttpResponse()

        endpointInvoker.invoke(handlerMethod, APIGatewayProxyRequestEvent())

        verify { method.call(argument) }
    }
}