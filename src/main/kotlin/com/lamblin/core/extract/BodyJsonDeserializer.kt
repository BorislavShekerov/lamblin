package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.OBJECT_MAPPER
import com.lamblin.core.exception.RequestPayloadParseException
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.annotation.RequestBody
import org.slf4j.LoggerFactory
import java.io.IOException
import java.lang.reflect.Parameter

private val LOGGER = LoggerFactory.getLogger(BodyJsonDeserializer::class.java)

internal interface BodyJsonDeserializer {

    /**
     * Attempts to deserialize the JSON into a variable, using the parameter to define the target variable type.
     */
    fun deserializeBodyJson(parameters: Array<Parameter>, bodyJson: String): Pair<String, Any>

    /**
     * Checks if the request body(if present) can be desiralized into a parameter defined in the [HandlerMethod]
     */
    fun canDeserializeRequestBody(handlerMethod: HandlerMethod, request: APIGatewayProxyRequestEvent): Boolean
}

/**
 * The mechanism responsible for deserializing a JSON string
 * into a variable of type defined by a given endpoint parameter.
 */
internal object DefaultBodyJsonDeserializer : BodyJsonDeserializer {

    override fun canDeserializeRequestBody(
            handlerMethod: HandlerMethod,
            request: APIGatewayProxyRequestEvent): Boolean {

        return HttpMethod.POST === handlerMethod.httpMethod
                && handlerMethod.method.parameters.isNotEmpty()
                && handlerMethod.method.parameters.any { parameter -> parameter.annotations.any { it is RequestBody } }
                && request.body.isNotEmpty()
    }

    override fun deserializeBodyJson(parameters: Array<Parameter>, bodyJson: String): Pair<String, Any> {
        LOGGER.debug("Attempting to deserialize request body")

        return parameters.find {
            it.annotations.any { annotation -> annotation is RequestBody }
        }?.let {
            it.name!! to deserializeBody(it, bodyJson)
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
