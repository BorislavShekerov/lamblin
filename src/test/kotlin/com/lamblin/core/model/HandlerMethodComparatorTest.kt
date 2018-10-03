package com.lamblin.core.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HandlerMethodComparatorTest {

    private val handlerMethodComparator = HandlerMethodComparator()

    @Test
    fun `shorter path should take precedence`() {
        val handleMethod1 = createHandlerMethod("/path")
        val handleMethod2 = createHandlerMethod("/path/longer")

        assertThat(handlerMethodComparator.compare(handleMethod1, handleMethod2)).isEqualTo(-1)
    }

    @Test
    fun `when 2 paths of the same length are compared, the one with less path params is chosen`() {
        val handleMethod1 = createHandlerMethod("/path/result")
        val handleMethod2 = createHandlerMethod("/path/{param}")

        assertThat(handlerMethodComparator.compare(handleMethod1, handleMethod2)).isEqualTo(-1)
    }

    @Test
    fun `when the 2 paths are the same, the one with more query params takes precedence`() {
        val handleMethod1 = createHandlerMethod(
                "/path/result?param1=query1",
                mapOf("param1" to HandlerMethodParameter(
                        annotationMappedName = "param1",
                        name = "param1",
                        required = true,
                        type = String::class.java)))

        val handleMethod2 = createHandlerMethod(
                "/path/result?param1=query1&param2=param2",
                mapOf(
                        "param1" to HandlerMethodParameter(
                                annotationMappedName = "param1",
                                name = "param1",
                                required = true,
                                type = String::class.java),
                        "param2" to HandlerMethodParameter(
                                annotationMappedName = "param2",
                                name = "param2",
                                required = true,
                                type = String::class.java)))

        assertThat(handlerMethodComparator.compare(handleMethod1, handleMethod2)).isEqualTo(1)
    }

    @Test
    fun `should be equal when same path, no query and path params`() {
        val handleMethod1 = createHandlerMethod("/path")
        val handleMethod2 = createHandlerMethod("/path")

        assertThat(handlerMethodComparator.compare(handleMethod1, handleMethod2)).isEqualTo(0)
    }

    @Test
    fun `should be equal when same path, and path params`() {
        val handleMethod1 = createHandlerMethod("/path/{param}")
        val handleMethod2 = createHandlerMethod("/path/{param}")

        assertThat(handlerMethodComparator.compare(handleMethod1, handleMethod2)).isEqualTo(0)
    }

    @Test
    fun `should be equal when same path,query and path params`() {
        val handleMethod1 = createHandlerMethod(
                "/path/{param}?query=query",
                mapOf("query" to HandlerMethodParameter(
                        annotationMappedName = "query",
                        name = "query",
                        required = true,
                        type = String::class.java)))
        val handleMethod2 = createHandlerMethod(
                "/path/{param}?query=query",
                mapOf("query" to HandlerMethodParameter(
                        annotationMappedName = "query",
                        name = "query",
                        required = true,
                        type = String::class.java)))

        assertThat(handlerMethodComparator.compare(handleMethod1, handleMethod2)).isEqualTo(0)
    }

    @Test
    fun `should be equal when same path, and query params, no path param`() {
        val handleMethod1 = createHandlerMethod(
                "/path?query=query",
                mapOf("query" to HandlerMethodParameter(
                        annotationMappedName = "query",
                        name = "query",
                        required = true,
                        type = String::class.java)))
        val handleMethod2 = createHandlerMethod(
                "/path?query=query",
                mapOf("query" to HandlerMethodParameter(
                        annotationMappedName = "query",
                        name = "query",
                        required = true,
                        type = String::class.java)))


        assertThat(handlerMethodComparator.compare(handleMethod1, handleMethod2)).isEqualTo(0)
    }

    private fun createHandlerMethod(path: String,
                                    params: Map<String, HandlerMethodParameter> = mapOf()) = HandlerMethod(
            path,
            HttpMethod.GET,
            params,
            method = HandlerMethodComparatorTest::class.java.declaredMethods.first(),
            controllerClass = HandlerMethodComparatorTest::class.java)

}
