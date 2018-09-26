package com.lamblin.core

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.reflect.Method

class EndpointInvokerClassTest {

    private val requestToParamValueMapper: RequestToParamValueMapper = mockk(relaxed = true)
    private val controllerRegistry: ControllerRegistry = mockk(relaxed = true)

    private val endpointInvoker = EndpointInvoker(requestToParamValueMapper, controllerRegistry)

    @Test
    fun `should throw IllegalStateException if controller for class not found`() {
        val handlerMethod = createHandlerMethod("/path")

        every { controllerRegistry.controllerForClass(TestController::class.java) } returns null

        assertThrows<IllegalStateException> { endpointInvoker.invoke(handlerMethod, APIGatewayProxyRequestEvent()) }
    }

    @Test
    fun `should call method with no parameters if the method does not have any parameters`() {
        val handlerMethod = createHandlerMethod("/path")

        every { controllerRegistry.controllerForClass(TestController::class.java) } returns TestController()

        val result = endpointInvoker.invoke(handlerMethod, APIGatewayProxyRequestEvent())

        assertThat(result.body).isEqualTo("")
    }

    @Test
    fun `should call method with required parameters`() {
        val handlerMethod = createHandlerMethod("/path", TestController::class.java.declaredMethods[1])

        every { controllerRegistry.controllerForClass(EndpointInvokerClassTest::class.java) } returns TestController()

        val request = APIGatewayProxyRequestEvent()
        val argument = "arg"
        every { requestToParamValueMapper.mapRequestParamsToValues(request, handlerMethod) } returns arrayOf(argument)

        val result = endpointInvoker.invoke(handlerMethod, APIGatewayProxyRequestEvent())

        assertThat(result.body).isEqualTo(argument)
    }

    private fun createHandlerMethod(
            path: String,
            method: Method = TestController::class.java.declaredMethods.first()) =
            HandlerMethod(
                    path,
                    HttpMethod.GET,
                    method = method,
                    controllerClass = TestController::class.java)

    class TestController {

        fun testEndpointNoParams(): HttpResponse<String> {
            return HttpResponse(body = "")
        }

        fun testEndpointWithParam(param: String): HttpResponse<String> {
            return HttpResponse(body = param)
        }
    }
}
