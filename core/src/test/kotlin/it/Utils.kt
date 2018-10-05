package it

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.lamblin.core.FrontController
import com.lamblin.core.OBJECT_MAPPER
import com.lamblin.core.model.HttpMethod
import org.assertj.core.api.Assertions.assertThat
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream

const val QUERY_PARAM_1 = "query_1"
const val QUERY_PARAM_2 = "query_2"

const val PATH_PARAM_1 = "path_param_1"
const val PATH_PARAM_2 = "path_param_2"

data class ResponseEntity(val content: String)

data class ExampleRequestBody(val body: String)

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
        expectedStatusCode: Int = 200,
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
