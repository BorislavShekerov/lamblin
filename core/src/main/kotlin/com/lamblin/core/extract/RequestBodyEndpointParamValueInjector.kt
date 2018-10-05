package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.OBJECT_MAPPER
import com.lamblin.core.exception.RequestPayloadParseException
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.REQUEST_BODY_MAPPED_NAME
import com.lamblin.core.model.annotation.RequestBody
import org.slf4j.LoggerFactory
import java.io.IOException
import java.lang.reflect.Parameter

private val LOGGER = LoggerFactory.getLogger(RequestBodyEndpointParamValueInjector::class.java)


/** Defines a mechanism for deciding the values of request body (i.e. parameters annotated with [RequestBody]). */
object RequestBodyEndpointParamValueInjector : EndpointParamValueInjector {

    private val requestBodyEnabledHttpMethods = setOf(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH)

    override fun injectParamValues(
            request: APIGatewayProxyRequestEvent,
            handlerMethod: HandlerMethod,
            paramAnnotationMappedNameToParam: Map<String, Parameter>) =

            if (canDeserializeRequestBody(handlerMethod, request)) {
                mapOf(deserializeBodyJson(paramAnnotationMappedNameToParam.values, request.body))
            } else mapOf()


    private fun canDeserializeRequestBody(
            handlerMethod: HandlerMethod,
            request: APIGatewayProxyRequestEvent): Boolean {

        return handlerMethod.httpMethod in requestBodyEnabledHttpMethods
                && handlerMethod.method.parameters.isNotEmpty()
                && handlerMethod.method.parameters.any { parameter -> parameter.annotations.any { it is RequestBody } }
                && request.body.isNotEmpty()
    }

    private fun deserializeBodyJson(parameters: Iterable<Parameter>, bodyJson: String): Pair<String, Any> {
        LOGGER.debug("Attempting to deserialize request body")

        return parameters.find {
            it.annotations.any { annotation -> annotation is RequestBody }
        }?.let {
            REQUEST_BODY_MAPPED_NAME to deserializeBody(it, bodyJson)
        } ?: throw IllegalStateException("Attempting to deserialize non-existent body param")
    }

    private fun deserializeBody(parameter: Parameter, bodyJson: String): Any {
        val lastParamType = parameter.type

        try {
            LOGGER.debug("Deserializing [{}] into [{}]", bodyJson, parameter.type)
            return OBJECT_MAPPER.readValue(bodyJson, lastParamType)
        } catch (e: IOException) {
            throw RequestPayloadParseException("Unable to parse request body JSON $bodyJson", e)
        }
    }
}
