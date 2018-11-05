/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package core

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.ControllerRegistry
import com.lamblin.core.EndpointInvoker
import com.lamblin.core.extract.EndpointParamValueInjector
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HandlerMethodParameter
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.KCallable
import kotlin.reflect.full.valueParameters

class EndpointInvokerTest {

    private val endpointParamValueInjector: EndpointParamValueInjector = mockk(relaxed = true)
    private val controllerRegistry: ControllerRegistry = mockk(relaxed = true)

    private val endpointInvoker = EndpointInvoker(endpointParamValueInjector, controllerRegistry)

    @BeforeEach
    fun setUp() {
        clearMocks(endpointParamValueInjector, controllerRegistry)
    }

    @Test
    fun `should throw IllegalStateException if controller for class not found`() {
        val handlerMethod = createHandlerMethod(
            "/path",
            TestController::class.members.find { it.name == "testEndpointNoParams" } as KCallable<HttpResponse<*>>)

        every { controllerRegistry.controllerForClass(TestController::class) } returns null

        assertThrows<IllegalStateException> { endpointInvoker.invoke(handlerMethod, APIGatewayProxyRequestEvent()) }
    }

    @Test
    fun `should call method with no parameters if the method does not have any parameters`() {
        val handlerMethod = createHandlerMethod(
            "/path",
            TestController::class.members.find { it.name == "testEndpointNoParams" } as KCallable<HttpResponse<*>>)

        every {
            controllerRegistry.controllerForClass(
                TestController::class)
        } returns TestController()

        val result = endpointInvoker.invoke(handlerMethod, APIGatewayProxyRequestEvent())

        assertThat(result.body).isEqualTo("")
    }

    @Test
    fun `should call method with required parameters`() {
        print(TestController::class.members.forEach { println(it.name) })
        val methodWithParam = TestController::class.members.find { it.name == "testEndpointWithParam" }!!

        val handlerMethod = createHandlerMethod(
            "/path",
            methodWithParam as KCallable<HttpResponse<*>>,
            mapOf(
                "param" to HandlerMethodParameter(
                    annotationMappedName = "test", name = "param",
                    type = String::class)))

        every {
            controllerRegistry.controllerForClass(
                TestController::class)
        } returns TestController()

        val request = APIGatewayProxyRequestEvent()
        val argument = "arg"

        val annotationMappedNameToParam = mapOf("test" to methodWithParam.valueParameters[0])

        every {
            endpointParamValueInjector.injectParamValues(
                request,
                handlerMethod,
                annotationMappedNameToParam)
        } returns mapOf("test" to argument)

        val result = endpointInvoker.invoke(handlerMethod, APIGatewayProxyRequestEvent())

        assertThat(result.body).isEqualTo(argument)
    }

    private fun createHandlerMethod(
        path: String,
        method: KCallable<HttpResponse<*>>,
        paramNameToParam: Map<String, HandlerMethodParameter> = mapOf()
    ) =
        HandlerMethod(
            path,
            HttpMethod.GET,
            paramNameToParam,
            method = method,
            controllerClass = TestController::class)

    class TestController {
        fun testEndpointNoParams(): HttpResponse<String> {
            return HttpResponse(body = "")
        }

        fun testEndpointWithParam(param: String): HttpResponse<String> {
            return HttpResponse(body = param)
        }
    }
}
