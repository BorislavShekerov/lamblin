package com.lamblin.core.model

import com.lamblin.core.DefaultBodyJsonToParamDeserializer
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.lang.reflect.Parameter

class DefaultBodyJsonToParamDeserializerTest {

    @Test
    fun `should throw RequestPayloadParseException when payload JSON invalid`() {
        val parameter: Parameter = mockk()

        DefaultBodyJsonToParamDeserializer.deserializeBodyJsonForParameter(parameter, "{'foo':")
    }
}