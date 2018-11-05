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
import java.util.Objects.nonNull
import kotlin.reflect.full.findAnnotation

private const val PATH_PARAM_VALUE = "param"
private const val QUERY_PARAM_WITH_DEFAULT_VALUE_MAPPED_NAME = "queryParam"
private const val QUERY_PARAM_WITHOUT_DEFAULT_VALUE_MAPPED_NAME = "queryParam"
private const val QUERY_PARAM_DEFAULT_VALUE = "defaultValue"
private const val HEADER_NAME = "headerName"

class HandlerMethodParameterTest {

    @Test
    fun `should handle request path param`() {
        val paramName = "test"
        val pathParamParameter = TestController::class.members.find { it.name == "test endpoint" }!!.parameters.find { nonNull(it.findAnnotation<PathParam>()) }

        val param = HandlerMethodParameter.of(
            paramName,
            String::class,
            pathParamParameter!!.findAnnotation<PathParam>())

        assertThat(param.type).isEqualTo(String::class)
        assertThat(param.name).isEqualTo(paramName)
        assertThat(param.annotationMappedName).isEqualTo(PATH_PARAM_VALUE)
    }


    @Test
    fun `should handle request query param with default value`() {
        val paramName = "test"
        val queryParamParameter = TestController::class.members.find { it.name == "test endpoint" }!!.parameters.find { it.name == "queryParamWithDefaultValue" }

        val param = HandlerMethodParameter.of(
            paramName,
            String::class,
            queryParamParameter!!.findAnnotation<QueryParam>())

        assertThat(param.type).isEqualTo(String::class)
        assertThat(param.name).isEqualTo(paramName)
        assertThat(param.annotationMappedName).isEqualTo(QUERY_PARAM_WITH_DEFAULT_VALUE_MAPPED_NAME)
        assertThat(param.required).isEqualTo(true)
        assertThat(param.defaultValue).isEqualTo(QUERY_PARAM_DEFAULT_VALUE)
    }

    @Test
    fun `should handle request query param without default value`() {
        val paramName = "test"
        val queryParamParameter = TestController::class.members.find { it.name == "test endpoint" }!!.parameters.find { it.name == "queryParamWithoutDefaultValue" }

        val param = HandlerMethodParameter.of(
            paramName,
            String::class,
            queryParamParameter!!.findAnnotation<QueryParam>())

        assertThat(param.type).isEqualTo(String::class)
        assertThat(param.name).isEqualTo(paramName)
        assertThat(param.annotationMappedName).isEqualTo(QUERY_PARAM_WITHOUT_DEFAULT_VALUE_MAPPED_NAME)
        assertThat(param.required).isEqualTo(true)
        assertThat(param.defaultValue).isEqualTo(null)
    }

    @Test
    fun `should handle request body param`() {
        val paramName = "test"
        val requestBodyParameter = TestController::class.members.find { it.name == "test endpoint" }!!.parameters.find { nonNull(it.findAnnotation<RequestBody>()) }

        val param = HandlerMethodParameter.of(
            paramName,
            String::class,
            requestBodyParameter!!.findAnnotation<RequestBody>())

        assertThat(param.name).isEqualTo(paramName)
        assertThat(param.type).isEqualTo(String::class)
        assertThat(param.annotationMappedName).isEqualTo(REQUEST_BODY_MAPPED_NAME)
    }

    @Test
    fun `should handle header param`() {
        val paramName = "test"
        val headerParam = TestController::class.members.find { it.name == "test endpoint" }!!.parameters.find { nonNull(it.findAnnotation<Header>()) }

        val param = HandlerMethodParameter.of(
            paramName,
            String::class,
            headerParam!!.findAnnotation<Header>())

        assertThat(param.name).isEqualTo(paramName)
        assertThat(param.type).isEqualTo(String::class)
        assertThat(param.annotationMappedName).isEqualTo(HEADER_NAME)
    }

    @Test
    fun `should handle APIGatewayProxyRequestEvent param`() {
        val paramName = "test"
        val param = HandlerMethodParameter.of(
            paramName,
            APIGatewayProxyRequestEvent::class,
            null)

        assertThat(param.name).isEqualTo(paramName)
        assertThat(param.type).isEqualTo(APIGatewayProxyRequestEvent::class)
        assertThat(param.annotationMappedName).isEqualTo(paramName)
    }

    @Test
    fun `should throw IllegalArgumentException if annotation not PathParam, QueryParam or RequestBody `() {
        val paramName = "test"
        assertThrows<IllegalArgumentException> {
            HandlerMethodParameter.of(
                paramName,
                String::class,
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
