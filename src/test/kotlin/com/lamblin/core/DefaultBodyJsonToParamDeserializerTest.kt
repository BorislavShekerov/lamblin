package com.lamblin.core

import com.lamblin.core.DefaultBodyJsonToParamDeserializer
import com.lamblin.core.exception.RequestPayloadParseException
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.reflect.Parameter

class DefaultBodyJsonToParamDeserializerTest {

    @Test
    fun `should throw RequestPayloadParseException when payload JSON invalid`() {
        val parameter: Parameter = mockk(relaxed = true)

        assertThrows<RequestPayloadParseException> {
            DefaultBodyJsonToParamDeserializer.deserializeBodyJsonForParameter(parameter, "{'foo':")
        }
    }
}