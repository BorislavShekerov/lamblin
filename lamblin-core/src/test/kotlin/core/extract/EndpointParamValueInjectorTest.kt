package core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.extract.EndpointParamValueInjector
import com.lamblin.core.model.HandlerMethod
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.lang.reflect.Parameter

class EndpointParamValueInjectorTest {

    private val endpointParamValueInjector = object: EndpointParamValueInjector {
        override fun injectParamValues(request: APIGatewayProxyRequestEvent,
            handlerMethod: HandlerMethod,
            paramAnnotationMappedNameToParam: Map<String, Parameter>): Map<String, Any> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    @Test
    fun `should cast param value to int`() {
        val value = endpointParamValueInjector.castParamToRequiredType(Int::class.java, "1")

        assertThat(value).isEqualTo(1)
    }

    @Test
    fun `should cast param value to double`() {
        val value = endpointParamValueInjector.castParamToRequiredType(Double::class.java, "1.3")

        assertThat(value).isEqualTo(1.3)
    }

    @Test
    fun `should cast param value to long`() {
        val value = endpointParamValueInjector.castParamToRequiredType(Long::class.java, "1")

        assertThat(value).isEqualTo(1L)
    }

    @Test
    fun `should cast param value to boolean`() {
        val value = endpointParamValueInjector.castParamToRequiredType(Boolean::class.java, "true")

        assertThat(value).isEqualTo(true)
    }
}
