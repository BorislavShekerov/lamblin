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
    fun `should cast param value to long`() {
        val value = endpointParamValueInjector.castParamToRequiredType(Long::class, "1")

        assertThat(value).isEqualTo(1L)
    }

    @Test
    fun `should cast param value to boolean`() {
        val value = endpointParamValueInjector.castParamToRequiredType(Boolean::class, "true")

        assertThat(value).isEqualTo(true)
    }
}
