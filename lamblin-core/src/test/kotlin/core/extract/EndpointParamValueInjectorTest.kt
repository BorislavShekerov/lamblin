package core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.extract.EndpointParamValueInjector
import com.lamblin.core.model.HandlerMethod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.KParameter

class EndpointParamValueInjectorTest {

    private val endpointParamValueInjector = object: EndpointParamValueInjector {
        override fun injectParamValues(request: APIGatewayProxyRequestEvent,
            handlerMethod: HandlerMethod,
            paramAnnotationMappedNameToParam: Map<String, KParameter>): Map<String, Any> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    @Test
    fun `should cast param value to byte`() {
        val value = endpointParamValueInjector.castParamToRequiredType(Byte::class, "1")

        assertThat(value).isEqualTo(1.toByte())
    }

    @Test
    fun `should cast param value to short`() {
        val value = endpointParamValueInjector.castParamToRequiredType(Short::class, "1")

        assertThat(value).isEqualTo(1.toShort())
    }

    @Test
    fun `should cast param value to int`() {
        val value = endpointParamValueInjector.castParamToRequiredType(Int::class, "1")

        assertThat(value).isEqualTo(1)
    }

    @Test
    fun `should cast param value to double`() {
        val value = endpointParamValueInjector.castParamToRequiredType(Double::class, "1.3")

        assertThat(value).isEqualTo(1.3)
    }

    @Test
    fun `should cast param value to float`() {
        val value = endpointParamValueInjector.castParamToRequiredType(Float::class, "1")

        assertThat(value).isEqualTo(1F)
    }

    @Test
    fun `should cast param value to long`() {
        val value = endpointParamValueInjector.castParamToRequiredType(Long::class, "1")

        assertThat(value).isEqualTo(1L)
    }

    @Test
    fun `should cast param value to char`() {
        val value = endpointParamValueInjector.castParamToRequiredType(Char::class, "1")

        assertThat(value).isEqualTo('1')
    }

    @Test
    fun `should cast param value to boolean`() {
        val value = endpointParamValueInjector.castParamToRequiredType(Boolean::class, "true")

        assertThat(value).isEqualTo(true)
    }

    @Test
    fun `should cast multivavalue to byte array and primitive`() {
        val valueArray = endpointParamValueInjector.castParamToRequiredType(Array<Byte>::class, arrayOf("1", "2"))

        assertThat(valueArray).isEqualTo(byteArrayOf(1, 2))

        val valuePrimitive = endpointParamValueInjector.castParamToRequiredType(Byte::class, arrayOf("1"))

        assertThat(valuePrimitive).isEqualTo(1.toByte())
    }

    @Test
    fun `should cast multivavalue to short array and primitive`() {
        val valueArray = endpointParamValueInjector.castParamToRequiredType(Array<Short>::class, arrayOf("1", "2"))

        assertThat(valueArray).isEqualTo(shortArrayOf(1, 2))

        val valuePrimitive = endpointParamValueInjector.castParamToRequiredType(Short::class, arrayOf("1"))

        assertThat(valuePrimitive).isEqualTo(1.toShort())
    }

    @Test
    fun `should cast multivavalue to int array and primitive`() {
        val valueArray = endpointParamValueInjector.castParamToRequiredType(Array<Int>::class, arrayOf("1", "2"))

        assertThat(valueArray).isEqualTo(intArrayOf(1, 2))

        val valuePrimitive = endpointParamValueInjector.castParamToRequiredType(Int::class, arrayOf("1"))

        assertThat(valuePrimitive).isEqualTo(1)
    }

    @Test
    fun `should cast multivavalue to long array and primitive`() {
        val valueArray = endpointParamValueInjector.castParamToRequiredType(Array<Long>::class, arrayOf("1", "2"))

        assertThat(valueArray).isEqualTo(longArrayOf(1, 2))

        val valuePrimitive = endpointParamValueInjector.castParamToRequiredType(Long::class, arrayOf("1"))

        assertThat(valuePrimitive).isEqualTo(1L)
    }

    @Test
    fun `should cast multivavalue to double array and primitive`() {
        val valueArray = endpointParamValueInjector.castParamToRequiredType(Array<Double>::class, arrayOf("1.2", "2.1"))

        assertThat(valueArray).isEqualTo(doubleArrayOf(1.2, 2.1))

        val valuePrimitive = endpointParamValueInjector.castParamToRequiredType(Double::class, arrayOf("1.2"))

        assertThat(valuePrimitive).isEqualTo(1.2)
    }

    @Test
    fun `should cast multivavalue to float array and primitive`() {
        val valueArray = endpointParamValueInjector.castParamToRequiredType(Array<Float>::class, arrayOf("1.2", "2.1"))

        assertThat(valueArray).isEqualTo(floatArrayOf(1.2f, 2.1f))

        val valuePrimitive = endpointParamValueInjector.castParamToRequiredType(Float::class, arrayOf("1.2"))

        assertThat(valuePrimitive).isEqualTo(1.2f)
    }

    @Test
    fun `should cast multivavalue to char array and primitive`() {
        val valueArray = endpointParamValueInjector.castParamToRequiredType(Array<Char>::class, arrayOf("a", "b"))

        assertThat(valueArray).isEqualTo(charArrayOf('a', 'b'))

        val valuePrimitive = endpointParamValueInjector.castParamToRequiredType(Char::class, arrayOf("a"))

        assertThat(valuePrimitive).isEqualTo('a')
    }

    @Test
    fun `should cast multivavalue to boolean array and primitive`() {
        val valueArray = endpointParamValueInjector.castParamToRequiredType(Array<Boolean>::class, arrayOf("true", "false"))

        assertThat(valueArray).isEqualTo(booleanArrayOf(true, false))

        val valuePrimitive = endpointParamValueInjector.castParamToRequiredType(Boolean::class, arrayOf("true"))

        assertThat(valuePrimitive).isEqualTo(true)
    }

    @Test
    fun `should cast multivavalue to string array and primitive`() {
        val valueArray = endpointParamValueInjector.castParamToRequiredType(Array<String>::class, arrayOf("foo", "bar"))

        assertThat(valueArray).isEqualTo(arrayOf("foo", "bar"))

        val valuePrimitive = endpointParamValueInjector.castParamToRequiredType(String::class, arrayOf("foo"))

        assertThat(valuePrimitive).isEqualTo("foo")
    }
}
