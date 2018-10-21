package core.model

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethodParameter
import com.lamblin.core.model.REQUEST_BODY_MAPPED_NAME
import com.lamblin.core.model.annotation.Header
import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import com.lamblin.core.model.annotation.RequestBody
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

private const val PATH_PARAM_VALUE = "param"
private const val QUERY_PARAM_WITH_DEFAULT_VALUE_MAPPED_NAME = "queryParam"
private const val QUERY_PARAM_WITHOUT_DEFAULT_VALUE_MAPPED_NAME = "queryParam"
private const val QUERY_PARAM_DEFAULT_VALUE = "defaultValue"
private const val HEADER_NAME = "headerName"

class HandlerMethodParameterTest {

    @Test
    fun `should handle request path param`() {
        val paramName = "test"
        val param = HandlerMethodParameter.of(
            paramName,
            String::class.java,
            TestController::class.java.methods[0].parameters[0].getAnnotation(PathParam::class.java))

        assertThat(param.type).isEqualTo(String::class.java)
        assertThat(param.name).isEqualTo(paramName)
        assertThat(param.annotationMappedName).isEqualTo(PATH_PARAM_VALUE)
    }


    @Test
    fun `should handle request query param with default value`() {
        val paramName = "test"
        val param = HandlerMethodParameter.of(
            paramName,
            String::class.java,
            TestController::class.java.methods[0].parameters[1].getAnnotation(QueryParam::class.java))

        assertThat(param.type).isEqualTo(String::class.java)
        assertThat(param.name).isEqualTo(paramName)
        assertThat(param.annotationMappedName).isEqualTo(QUERY_PARAM_WITH_DEFAULT_VALUE_MAPPED_NAME)
        assertThat(param.required).isEqualTo(true)
        assertThat(param.defaultValue).isEqualTo(QUERY_PARAM_DEFAULT_VALUE)
    }

    @Test
    fun `should handle request query param without default value`() {
        val paramName = "test"
        val param = HandlerMethodParameter.of(
            paramName,
            String::class.java,
            TestController::class.java.methods[0].parameters[2].getAnnotation(QueryParam::class.java))

        assertThat(param.type).isEqualTo(String::class.java)
        assertThat(param.name).isEqualTo(paramName)
        assertThat(param.annotationMappedName).isEqualTo(QUERY_PARAM_WITHOUT_DEFAULT_VALUE_MAPPED_NAME)
        assertThat(param.required).isEqualTo(true)
        assertThat(param.defaultValue).isEqualTo(null)
    }

    @Test
    fun `should handle request body param`() {
        val paramName = "test"
        val param = HandlerMethodParameter.of(
            paramName,
            String::class.java,
            TestController::class.java.methods[0].parameters[3].getAnnotation(RequestBody::class.java))

        assertThat(param.name).isEqualTo(paramName)
        assertThat(param.type).isEqualTo(String::class.java)
        assertThat(param.annotationMappedName).isEqualTo(REQUEST_BODY_MAPPED_NAME)
    }

    @Test
    fun `should handle header param`() {
        val paramName = "test"
        val param = HandlerMethodParameter.of(
            paramName,
            String::class.java,
            TestController::class.java.methods[0].parameters[4].getAnnotation(Header::class.java))

        assertThat(param.name).isEqualTo(paramName)
        assertThat(param.type).isEqualTo(String::class.java)
        assertThat(param.annotationMappedName).isEqualTo(HEADER_NAME)
    }

    @Test
    fun `should handle APIGatewayProxyRequestEvent param`() {
        val paramName = "test"
        val param = HandlerMethodParameter.of(
            paramName,
            APIGatewayProxyRequestEvent::class.java,
            null)

        assertThat(param.name).isEqualTo(paramName)
        assertThat(param.type).isEqualTo(APIGatewayProxyRequestEvent::class.java)
        assertThat(param.annotationMappedName).isEqualTo(paramName)
    }

    @Test
    fun `should throw IllegalArgumentException if annotation not PathParam, QueryParam or RequestBody `() {
        val paramName = "test"
        assertThrows<IllegalArgumentException> {
            HandlerMethodParameter.of(
                paramName,
                String::class.java,
                TestController::class.java.methods[0].parameters[5].getAnnotation(IllegalAnnotation::class.java))
        }
    }

    private class TestController {

        fun `test endpoint`(
            @PathParam(PATH_PARAM_VALUE) pathParam: String,
            @QueryParam(
                QUERY_PARAM_WITH_DEFAULT_VALUE_MAPPED_NAME,
                defaultValue = QUERY_PARAM_DEFAULT_VALUE) queryParamWithDefaultValue: String,
            @QueryParam(QUERY_PARAM_WITHOUT_DEFAULT_VALUE_MAPPED_NAME) queryParamWithoutDefaultValue: String,
            @RequestBody requestBody: String,
            @Header(HEADER_NAME) header: String,
            request: APIGatewayProxyRequestEvent,
            @IllegalAnnotation test: String) {
        }

    }

    @Target(AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class IllegalAnnotation
}
