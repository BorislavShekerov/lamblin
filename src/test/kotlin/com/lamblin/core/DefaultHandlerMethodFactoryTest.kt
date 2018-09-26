package com.lamblin.core

import com.lamblin.core.model.HandlerMethodParameter
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.annotation.Endpoint
import io.mockk.every
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

class DefaultHandlerMethodFactoryTest {

    @Test
    fun `method has no endpoint annotation`() {
        assertThrows<IllegalArgumentException> {
            DefaultHandlerMethodFactory.method(
                mockk(relaxed = true),
                mockk(relaxed = true))
        }
    }

    @Test
    fun `should create POST method handler when endpoint method POST`() {
        val methodMock: Method = mockk(relaxed = true)

        val endpointAnnotationMock: Endpoint = mockk()
        every { methodMock.annotations } returns arrayOf(endpointAnnotationMock)
        every { endpointAnnotationMock.method } returns HttpMethod.POST
        val endpointPath = "path"
        every { endpointAnnotationMock.path } returns endpointPath

        val endpointParameter: Parameter = mockk()
        every { methodMock.parameters } returns arrayOf(endpointParameter)
        val parameterName = "parameterName"
        every { endpointParameter.name } returns parameterName
        val parameterClass: Class<Parameter> = mockk()
        every { endpointParameter.javaClass } returns parameterClass
        val parameterAnnotation: Annotation = mockk()
        every { endpointParameter.annotations } returns arrayOf(parameterAnnotation)

        val controllerClassMock: Class<out Any> = mockk(relaxed = true)

        val handlerMethod = DefaultHandlerMethodFactory.method(methodMock, controllerClassMock)

        assertThat(handlerMethod.controllerClass).isEqualTo(controllerClassMock)
        assertThat(handlerMethod.method).isEqualTo(methodMock)
        assertThat(handlerMethod.httpMethod).isEqualTo(endpointPath)
        assertThat(handlerMethod.path).isEqualTo(endpointPath)
        assertThat(handlerMethod.paramNameToParam).isEqualTo(mapOf(parameterName to HandlerMethodParameter.of(parameterName, parameterClass, parameterAnnotation)))
    }
}