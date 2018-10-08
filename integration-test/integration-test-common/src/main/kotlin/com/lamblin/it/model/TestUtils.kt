package com.lamblin.it.model

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.lamblin.core.FrontController
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.StatusCode
import org.assertj.core.api.Assertions.assertThat
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

val OBJECT_MAPPER = ObjectMapper().apply {
    registerModule(JavaTimeModule())
    registerModule(KotlinModule())
}

const val QUERY_PARAM_1 = "query_1"
const val QUERY_PARAM_2 = "query_2"

const val PATH_PARAM_1 = "path_param_1"
const val PATH_PARAM_2 = "path_param_2"

fun createRequestInputStream(
        path: String,
        httpMethod: HttpMethod,
        body: Any) = createRequestInputStream(path, httpMethod, mapOf(), body)

@JvmOverloads
fun createRequestInputStream(
        path: String,
        httpMethod: HttpMethod,
        queryParams: Map<String, String> = mapOf(),
        body: Any? = null) =

        ByteArrayInputStream(OBJECT_MAPPER.writeValueAsBytes(APIGatewayProxyRequestEvent().apply {
            withPath(path)
            withHttpMethod(httpMethod.name)
            withQueryStringParamters(queryParams)
            body?.let { withBody(OBJECT_MAPPER.writeValueAsString(body)) }
        }))

fun runRequestAndVerifyResponse(
        frontController: FrontController,
        requestInputStream: InputStream,
        expectedResponseBodyContent: String) = runRequestAndVerifyResponse(frontController,
                                                                           requestInputStream,
                                                                           StatusCode.OK.code,
                                                                           expectedResponseBodyContent)

@JvmOverloads
fun runRequestAndVerifyResponse(
        frontController: FrontController,
        requestInputStream: InputStream,
        expectedStatusCode: Int = StatusCode.OK.code,
        expectedResponseBodyContent: String? = null) {

    val outputStream = ByteArrayOutputStream()

    frontController.handlerRequest(requestInputStream, outputStream)

    val result = outputStream.toString("UTF-8")
    val response = OBJECT_MAPPER.readValue(result, APIGatewayProxyResponseEvent::class.java)


    assertThat(response.statusCode).isEqualTo(expectedStatusCode)
    expectedResponseBodyContent?.let {
        assertThat(response.body).isEqualTo(
                OBJECT_MAPPER.writeValueAsString(ResponseEntity(it)))
    }
}

