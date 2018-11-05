/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package core

import com.lamblin.core.ControllerRegistry
import com.lamblin.core.Lamblin
import com.lamblin.core.handler.HandlerMethodFactory
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Controller
import com.lamblin.core.model.annotation.Endpoint
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.KCallable

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
                TestControllerWithEndpoints1::class.members.find { it.name == "endpoint_get" }!! as KCallable<HttpResponse<*>>,
                TestControllerWithEndpoints1::class)
        } returns getHandlerMethod
        every {
            handlerMethodFactory.method(
                TestControllerWithEndpoints1::class.members.find { it.name == "endpoint_post" } as KCallable<HttpResponse<*>>,
                TestControllerWithEndpoints1::class)
        } returns postHandlerMethod
        every { controllerRegistry.controllerClasses() } returns listOf(
            TestControllerWithEndpoints1::class)

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
                TestControllerWithEndpoints1::class.members.find { it.name == "endpoint_get" } as KCallable<HttpResponse<*>>,
                TestControllerWithEndpoints1::class)
        } returns getHandlerMethod
        every {
            handlerMethodFactory.method(
                TestControllerWithEndpoints1::class.members.find { it.name == "endpoint_post" } as KCallable<HttpResponse<*>>,
                TestControllerWithEndpoints1::class)
        } returns postHandlerMethod

        every {
            handlerMethodFactory.method(
                TestControllerWithEndpoints2::class.members.find { it.name == "endpoint_get" } as KCallable<HttpResponse<*>>,
                TestControllerWithEndpoints2::class)
        } returns getHandlerMethod2
        every {
            handlerMethodFactory.method(
                TestControllerWithEndpoints2::class.members.find { it.name == "endpoint_post" } as KCallable<HttpResponse<*>>,
                TestControllerWithEndpoints2::class)
        } returns postHandlerMethod2
        every { controllerRegistry.controllerClasses() } returns listOf(
            TestControllerWithEndpoints1::class,
            TestControllerWithEndpoints2::class)

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
            TestControllerNoEndpoints::class)

        val lamblin = Lamblin(
            mockk(),
            mockk(relaxed = true),
            controllerRegistry,
            mockk(relaxed = true))

        val httpMethodToMethodHandlers = lamblin.createHttpMethodToPathToHandlerMethodMap()

        assertThat(httpMethodToMethodHandlers).hasSize(0)
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
