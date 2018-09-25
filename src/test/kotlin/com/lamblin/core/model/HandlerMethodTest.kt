package com.lamblin.core.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class HandlerMethodTest {

    @Test
    fun `should match when path matches and no params`() {
        val handlerMethod = HandlerMethod(
                "/path",
                HttpMethod.GET,
                method = HandlerMethodTest::class.members.first(),
                controllerClass = HandlerMethodTest::class)

        val result = handlerMethod.matches("/path", mapOf())

        assertThat(result).isTrue()
    }

    @Test
    fun `should not match when request path longer than the handler method path`() {
        val handlerMethod = HandlerMethod(
                "/path",
                HttpMethod.GET,
                method = HandlerMethodTest::class.members.first(),
                controllerClass = HandlerMethodTest::class)

        val result = handlerMethod.matches("/path/subpath", mapOf())

        assertThat(result).isFalse()
    }

    @Test
    fun `should not match when request path differs the handler method path`() {
        val handlerMethod = HandlerMethod(
                "/path",
                HttpMethod.GET,
                method = HandlerMethodTest::class.members.first(),
                controllerClass = HandlerMethodTest::class)

        val result = handlerMethod.matches("/fail-2", mapOf())

        assertThat(result).isFalse()
    }

    @Test
    fun `should not match when same length but different paths`() {
        val handlerMethod = HandlerMethod(
                "/path",
                HttpMethod.GET,
                method = HandlerMethodTest::class.members.first(),
                controllerClass = HandlerMethodTest::class)

        val result = handlerMethod.matches("/fail", mapOf())

        assertThat(result).isFalse()
    }

    @Test
    fun `should match when path param used, path param in the end`() {
        val handlerMethod = HandlerMethod(
                "/path/{param}",
                HttpMethod.GET,
                mapOf("param" to HandlerMethodParameter(
                        annotationMappedName = "param",
                        name = "param",
                        type = String::class.java)),
                method = HandlerMethodTest::class.members.first(),
                controllerClass = HandlerMethodTest::class)

        val result = handlerMethod.matches("/path/foo", mapOf())

        assertThat(result).isTrue()
    }

    @Test
    fun `should match when path param used, path param in the middle`() {
        val handlerMethod = HandlerMethod(
                "/path/{param}/remaining",
                HttpMethod.GET,
                mapOf("param" to HandlerMethodParameter(
                        annotationMappedName = "param",
                        name = "param",
                        type = String::class.java)),
                method = HandlerMethodTest::class.members.first(),
                controllerClass = HandlerMethodTest::class)

        val result = handlerMethod.matches("/path/foo/remaining", mapOf())

        assertThat(result).isTrue()
    }

    @Test
    fun `should not match when paths match but required query param not present`() {
        val handlerMethod = HandlerMethod(
                "/path/{param}/remaining",
                HttpMethod.GET,
                mapOf(
                        "param" to HandlerMethodParameter(
                                annotationMappedName = "param",
                                name = "param",
                                type = String::class.java),
                        "query" to HandlerMethodParameter(name = "query", required = true, type = String::class.java)),
                method = HandlerMethodTest::class.members.first(),
                controllerClass = HandlerMethodTest::class)

        val result = handlerMethod.matches("/path/foo/remaining", mapOf())

        assertThat(result).isFalse()
    }

    @Test
    fun `should match when paths match, required query param not present but a default value present`() {
        val handlerMethod = HandlerMethod(
                "/path/{param}/remaining",
                HttpMethod.GET,
                mapOf(
                        "param" to HandlerMethodParameter(
                                annotationMappedName = "param",
                                name = "param",
                                type = String::class.java),
                        "query" to HandlerMethodParameter(
                                name = "query",
                                required = true,
                                type = String::class.java,
                                defaultValue = "test")),
                method = HandlerMethodTest::class.members.first(),
                controllerClass = HandlerMethodTest::class)

        val result = handlerMethod.matches("/path/foo/remaining", mapOf())

        assertThat(result).isTrue()
    }

    @Test
    fun `should match when paths match and non-required query param not present`() {
        val handlerMethod = HandlerMethod(
                "/path/{param}/remaining",
                HttpMethod.GET,
                mapOf(
                        "param" to HandlerMethodParameter(
                                annotationMappedName = "param",
                                name = "param",
                                type = String::class.java),
                        "query" to HandlerMethodParameter(name = "query", required = false, type = String::class.java)),
                method = HandlerMethodTest::class.members.first(),
                controllerClass = HandlerMethodTest::class)

        val result = handlerMethod.matches("/path/foo/remaining", mapOf())

        assertThat(result).isTrue()
    }

    @Test
    fun `should match when paths match and required query param present`() {
        val handlerMethod = HandlerMethod(
                "/path/{param}/remaining",
                HttpMethod.GET,
                mapOf(
                        "param" to HandlerMethodParameter(
                                annotationMappedName = "param",
                                name = "param",
                                type = String::class.java),
                        "query" to HandlerMethodParameter(
                                annotationMappedName = "query",
                                name = "query",
                                required = true,
                                type = String::class.java)),
                method = HandlerMethodTest::class.members.first(),
                controllerClass = HandlerMethodTest::class)

        val result = handlerMethod.matches("/path/foo/remaining", mapOf("query" to "test"))

        assertThat(result).isTrue()
    }

}