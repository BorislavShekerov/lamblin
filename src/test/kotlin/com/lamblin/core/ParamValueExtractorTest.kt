package com.lamblin.core

import com.lamblin.core.extract.BodyJsonDeserializer
import com.lamblin.core.extract.ParamValueExtractor
import io.mockk.mockk
import org.junit.jupiter.api.Test

class ParamValueExtractorTest {

    private val bodyJsonDeserializer: BodyJsonDeserializer = mockk()

    private val paramValueExtractor = ParamValueExtractor(bodyJsonDeserializer)

    @Test
    fun `should extract params when query params only`() {

    }

    @Test
    fun `should extract long query param`() {

    }

    @Test
    fun `should extract boolean query param`() {

    }

    @Test
    fun `should extract int query param`() {

    }

    @Test
    fun `should extract path parameters`() {

    }

    @Test
    fun `should extract `() {

    }

}