package com.lamblin.local.runner

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.fasterxml.jackson.databind.ObjectMapper
import com.lamblin.core.FrontController
import com.lamblin.core.model.StatusCode
import io.javalin.Context
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

internal val OBJECT_MAPPER = ObjectMapper()
private val LOGGER = LoggerFactory.getLogger(FrontControllerDelegator::class.java)

class FrontControllerDelegator(
    internal val frontController: FrontController,
    private val objetMapper: ObjectMapper = OBJECT_MAPPER
) {

    internal fun delegateToController(context: Context) {
        val requestEvent = createRequestEvent(context)

        val responseOutputStream = ByteArrayOutputStream()

        try {
            frontController.handlerRequest(
                ByteArrayInputStream(objetMapper.writeValueAsBytes(requestEvent)),
                responseOutputStream)

            val response = objetMapper.readValue(
                String(responseOutputStream.toByteArray()),
                APIGatewayProxyResponseEvent::class.java)

            context.status(response.statusCode)
            response?.headers?.forEach { context.header(it.key, it.value) }
            response.body?.let { context.result(it) }
        } catch (e: RuntimeException) {
            LOGGER.error("Error occurred while handling request", e)
            context.status(StatusCode.API_ERROR.code)
        }
    }

    private fun createRequestEvent(context: Context) = APIGatewayProxyRequestEvent().apply {
        httpMethod = context.method()
        path = context.path()
        queryStringParameters = context.queryParamMap().mapValues { it.value[0] }
        body = context.body()
        headers = context.headerMap()
    }
}
