/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package core

import com.lamblin.core.ControllerRegistry
import com.lamblin.core.Lamblin
import com.lamblin.core.OBJECT_MAPPER
import com.lamblin.core.PluginRegistry
import com.lamblin.core.handler.HandlerMethodFactory
import com.lamblin.core.handler.RequestHandler
import com.lamblin.core.handler.RequestHandlerAdapter
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.annotation.Controller
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.plugin.core.model.PluginExecutionResult
import com.lamblin.plugin.core.model.PluginType
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream

const val PATH_GET_1 = "path_get_1"
const val PATH_POST_1 = "path_post_1"
const val PATH_GET_2 = "path_get_2"
const val PATH_POST_2 = "path_post_2"

class LamblinTest {

    private val getHandlerMethod: HandlerMethod = mockk()
    private val postHandlerMethod: HandlerMethod = mockk()
    private val getHandlerMethod2: HandlerMethod = mockk()
    private val postHandlerMethod2: HandlerMethod = mockk()

    init {
        every { getHandlerMethod.httpMethod } returns HttpMethod.GET
        every { postHandlerMethod.httpMethod } returns HttpMethod.POST

        every { getHandlerMethod2.httpMethod } returns HttpMethod.GET
        every { postHandlerMethod2.httpMethod } returns HttpMethod.POST
    }

    @Test
    fun `companion should create a valid instance`() {
        val instance = Lamblin.frontController(TestControllerWithEndpoints1())

        assertThat(instance).isNotNull
    }

    @Test
    fun `should create handler methods for all endpoints in controllers`() {
        val handlerMethodFactory: HandlerMethodFactory = mockk(relaxed = true)
        val controllerRegistry: ControllerRegistry = mockk()

        every {
            handlerMethodFactory.method(
                TestControllerWithEndpoints1::class.java.declaredMethods[0],
                TestControllerWithEndpoints1::class.java)
        } returns getHandlerMethod
        every {
            handlerMethodFactory.method(
                TestControllerWithEndpoints1::class.java.declaredMethods[1],
                TestControllerWithEndpoints1::class.java)
        } returns postHandlerMethod
        every { controllerRegistry.controllerClasses() } returns listOf(
            TestControllerWithEndpoints1::class.java)

        val lamblin = Lamblin(
            mockk(),
            handlerMethodFactory,
            controllerRegistry,
            mockk(relaxed = true))

        val httpMethodToMethodHandlers = lamblin.createHttpMethodToPathToHandlerMethodMap()

        assertThat(httpMethodToMethodHandlers).hasSize(2)
        assertThat(httpMethodToMethodHandlers[HttpMethod.GET]).hasSize(1)
        assertThat(httpMethodToMethodHandlers[HttpMethod.POST]).hasSize(1)
    }

    @Test
    fun `should create handler methods for all endpoints in multiple controller`() {
        val handlerMethodFactory: HandlerMethodFactory = mockk(relaxed = true)
        val controllerRegistry: ControllerRegistry = mockk()

        every {
            handlerMethodFactory.method(
                TestControllerWithEndpoints1::class.java.declaredMethods[0],
                TestControllerWithEndpoints1::class.java)
        } returns getHandlerMethod
        every {
            handlerMethodFactory.method(
                TestControllerWithEndpoints1::class.java.declaredMethods[1],
                TestControllerWithEndpoints1::class.java)
        } returns postHandlerMethod

        every {
            handlerMethodFactory.method(
                TestControllerWithEndpoints2::class.java.declaredMethods[0],
                TestControllerWithEndpoints2::class.java)
        } returns getHandlerMethod2
        every {
            handlerMethodFactory.method(
                TestControllerWithEndpoints2::class.java.declaredMethods[1],
                TestControllerWithEndpoints2::class.java)
        } returns postHandlerMethod2
        every { controllerRegistry.controllerClasses() } returns listOf(
            TestControllerWithEndpoints1::class.java,
            TestControllerWithEndpoints2::class.java)

        val lamblin = Lamblin(
            mockk(),
            handlerMethodFactory,
            controllerRegistry,
            mockk(relaxed = true))

        val httpMethodToMethodHandlers = lamblin.createHttpMethodToPathToHandlerMethodMap()

        assertThat(httpMethodToMethodHandlers).hasSize(2)
        assertThat(httpMethodToMethodHandlers[HttpMethod.GET]).hasSize(2)
        assertThat(httpMethodToMethodHandlers[HttpMethod.POST]).hasSize(2)
    }

    @Test
    fun `should not create handler methods if no endpoints in controller`() {
        val controllerRegistry: ControllerRegistry = mockk()

        every { controllerRegistry.controllerClasses() } returns listOf(
            TestControllerNoEndpoints::class.java)

        val lamblin = Lamblin(
            mockk(),
            mockk(relaxed = true),
            controllerRegistry,
            mockk(relaxed = true))

        val httpMethodToMethodHandlers = lamblin.createHttpMethodToPathToHandlerMethodMap()

        assertThat(httpMethodToMethodHandlers).hasSize(0)
    }

    @Test
    fun `should use warmup plugin if request warmup and plugin registered`() {
        val controllerRegistry: ControllerRegistry = mockk()
        val requestHandlerAdapter: RequestHandlerAdapter = mockk()

        every { controllerRegistry.controllerClasses() } returns listOf(
            TestControllerNoEndpoints::class.java)

        val pluginRegistryMock: PluginRegistry = mockk(relaxed = true)

        val eventContents = mapOf("warmup" to "warmup")
        val eventInput = ByteArrayInputStream(OBJECT_MAPPER.writeValueAsBytes(eventContents))

        every { pluginRegistryMock.isPluginRegistered(PluginType.WARMUP) } returns true
        every { pluginRegistryMock.executePlugin(PluginType.WARMUP, eventContents) } returns PluginExecutionResult.SUCCESS

        val lamblin = Lamblin(
            requestHandlerAdapter,
            mockk(relaxed = true),
            controllerRegistry,
            pluginRegistryMock)

        lamblin.handlerRequest(eventInput, mockk())

        verify { pluginRegistryMock.executePlugin(PluginType.WARMUP, eventContents) }
        verify(exactly = 0) { requestHandlerAdapter.handlerRequest(any(), any(), any())  }
    }

    @Test
    fun `should handle request if warmup plugin configured but event not warmup`() {
        val controllerRegistry: ControllerRegistry = mockk()
        val requestHandlerAdapter: RequestHandlerAdapter = mockk(relaxed = true)

        every { controllerRegistry.controllerClasses() } returns listOf(
            TestControllerNoEndpoints::class.java)

        val pluginRegistryMock: PluginRegistry = mockk(relaxed = true)

        val eventContents = mapOf("foo" to "bar")
        val eventInput = ByteArrayInputStream(OBJECT_MAPPER.writeValueAsBytes(eventContents))

        every { pluginRegistryMock.isPluginRegistered(PluginType.WARMUP) } returns true
        every { pluginRegistryMock.executePlugin(PluginType.WARMUP, eventContents) } returns PluginExecutionResult.SKIPPED

        val lamblin = Lamblin(
            requestHandlerAdapter,
            mockk(relaxed = true),
            controllerRegistry,
            pluginRegistryMock)

        lamblin.handlerRequest(eventInput, mockk())

        verify { pluginRegistryMock.executePlugin(PluginType.WARMUP, eventContents) }
        verify{ requestHandlerAdapter.handlerRequest(eventContents, any(), lamblin.httpMethodToHandlers)  }
    }

    @Test
    fun `should handle request if warmup plugin not configured`() {
        val controllerRegistry: ControllerRegistry = mockk()
        val requestHandlerAdapter: RequestHandlerAdapter = mockk(relaxed = true)

        every { controllerRegistry.controllerClasses() } returns listOf(
            TestControllerNoEndpoints::class.java)

        val pluginRegistryMock: PluginRegistry = mockk(relaxed = true)

        val eventContents = mapOf("foo" to "bar")
        val eventInput = ByteArrayInputStream(OBJECT_MAPPER.writeValueAsBytes(eventContents))

        val lamblin = Lamblin(
            requestHandlerAdapter,
            mockk(relaxed = true),
            controllerRegistry,
            mockk(relaxed = true))

        lamblin.handlerRequest(eventInput, mockk())

        verify(exactly = 0) { pluginRegistryMock.executePlugin(PluginType.WARMUP, eventContents) }
        verify{ requestHandlerAdapter.handlerRequest(eventContents, any(), lamblin.httpMethodToHandlers)  }
    }
}

@Controller
class TestControllerWithEndpoints1 {

    @Endpoint(PATH_GET_1, method = HttpMethod.GET)
    fun endpoint_get() {
    }

    @Endpoint(PATH_POST_1, method = HttpMethod.POST)
    fun endpoint_post() {
    }

}

@Controller
class TestControllerWithEndpoints2 {

    @Endpoint(PATH_GET_2, method = HttpMethod.GET)
    fun endpoint_get() {
    }

    @Endpoint(PATH_POST_2, method = HttpMethod.POST)
    fun endpoint_post() {
    }
}

class TestControllerNoEndpoints
