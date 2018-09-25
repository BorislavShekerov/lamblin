package com.lamblin.core.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HandlerMethodComparatorTest {

    val handlerMethodComparator = HandlerMethodComparator()

    @Test
    fun `shorter path should take precedence`() {
        val handleMethod1 = HandlerMethod(
                "/path",
                HttpMethod.GET,
                method = HandlerMethodComparatorTest::class.members.first(),
                controllerClass = HandlerMethodComparatorTest::class)

        val handleMethod2 = HandlerMethod(
                "/path/longer",
                HttpMethod.GET,
                method = HandlerMethodComparatorTest::class.members.first(),
                controllerClass = HandlerMethodComparatorTest::class)

        assertThat(handlerMethodComparator.compare(handleMethod1, handleMethod2)).isEqualTo(-1)
    }

    @Test
    fun `when 2 paths of the same length are compared, the one with less path params is chosen`() {
        val handleMethod1 = HandlerMethod(
                "/path/result",
                HttpMethod.GET,
                method = HandlerMethodComparatorTest::class.members.first(),
                controllerClass = HandlerMethodComparatorTest::class)

        val handleMethod2 = HandlerMethod(
                "/path/{param}",
                HttpMethod.GET,
                method = HandlerMethodComparatorTest::class.members.first(),
                controllerClass = HandlerMethodComparatorTest::class)

        assertThat(handlerMethodComparator.compare(handleMethod1, handleMethod2)).isEqualTo(-1)
    }

    @Test
    fun `when the 2 paths are the same, the one with more query params takes precedence`() {
        val handleMethod1 = HandlerMethod(
                "/path/result?param1=query1",
                HttpMethod.GET,
                mapOf("param1" to HandlerMethodParameter(name = "param1", required = true, type = String::class.java)),
                method = HandlerMethodComparatorTest::class.members.first(),
                controllerClass = HandlerMethodComparatorTest::class)

        val handleMethod2 = HandlerMethod(
                "/path/result?param1=query1&param2=param2",
                HttpMethod.GET,
                mapOf(
                        "param1" to HandlerMethodParameter(name = "param1", required = true, type = String::class.java),
                        "param2" to HandlerMethodParameter(name = "param2", required = true, type = String::class.java)),
                method = HandlerMethodComparatorTest::class.members.first(),
                controllerClass = HandlerMethodComparatorTest::class)

        assertThat(handlerMethodComparator.compare(handleMethod1, handleMethod2)).isEqualTo(1)
    }

    @Test
    fun `should be equal when same path, no query and path params`() {
        val handleMethod1 = HandlerMethod(
                "/path",
                HttpMethod.GET,
                method = HandlerMethodComparatorTest::class.members.first(),
                controllerClass = HandlerMethodComparatorTest::class)

        val handleMethod2 = HandlerMethod(
                "/path",
                HttpMethod.GET,
                method = HandlerMethodComparatorTest::class.members.first(),
                controllerClass = HandlerMethodComparatorTest::class)

        assertThat(handlerMethodComparator.compare(handleMethod1, handleMethod2)).isEqualTo(0)
    }

    @Test
    fun `should be equal when same path, and path params`() {
        val handleMethod1 = HandlerMethod(
                "/path/{param}",
                HttpMethod.GET,
                method = HandlerMethodComparatorTest::class.members.first(),
                controllerClass = HandlerMethodComparatorTest::class)

        val handleMethod2 = HandlerMethod(
                "/path/{param}",
                HttpMethod.GET,
                method = HandlerMethodComparatorTest::class.members.first(),
                controllerClass = HandlerMethodComparatorTest::class)

        assertThat(handlerMethodComparator.compare(handleMethod1, handleMethod2)).isEqualTo(0)
    }

    @Test
    fun `should be equal when same path,query and path params`() {
        val handleMethod1 = HandlerMethod(
                "/path/{param}?query=query",
                HttpMethod.GET,
                method = HandlerMethodComparatorTest::class.members.first(),
                controllerClass = HandlerMethodComparatorTest::class)

        val handleMethod2 = HandlerMethod(
                "/path/{param}?query=query",
                HttpMethod.GET,
                method = HandlerMethodComparatorTest::class.members.first(),
                controllerClass = HandlerMethodComparatorTest::class)

        assertThat(handlerMethodComparator.compare(handleMethod1, handleMethod2)).isEqualTo(0)
    }

    @Test
    fun `should be equal when same path, and query params, no path param`() {
        val handleMethod1 = HandlerMethod(
                "/path?query=query",
                HttpMethod.GET,
                method = HandlerMethodComparatorTest::class.members.first(),
                controllerClass = HandlerMethodComparatorTest::class)

        val handleMethod2 = HandlerMethod(
                "/path?query=query",
                HttpMethod.GET,
                method = HandlerMethodComparatorTest::class.members.first(),
                controllerClass = HandlerMethodComparatorTest::class)

        assertThat(handlerMethodComparator.compare(handleMethod1, handleMethod2)).isEqualTo(0)
    }
}