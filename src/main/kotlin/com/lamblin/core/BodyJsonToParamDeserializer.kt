package com.lamblin.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.lamblin.core.exception.RequestPayloadParseException
import org.slf4j.LoggerFactory
import java.io.IOException
import kotlin.reflect.KParameter

private val LOGGER = LoggerFactory.getLogger(BodyJsonToParamDeserializer::class.java)

internal interface BodyJsonToParamDeserializer {
    fun deserializeBodyJsonForParameter(parameter: KParameter, bodyJson: String): Any
}

/**
 * The mechanism responsible for deserializing a JSON string
 * into a variable of type defined by a given endpoint parameter.
 */
internal object DefaultBodyJsonToParamDeserializer : BodyJsonToParamDeserializer {

    /**
     * Attempts to deserialize the JSON into a variable, using the parameter to define the target variable type.
     */
    override fun deserializeBodyJsonForParameter(parameter: KParameter, bodyJson: String): Any {
        val lastParamType = parameter.type

        try {
            LOGGER.debug("Deserializing [{}] into [{}]", bodyJson, parameter.type)
            return ObjectMapper().readValue(bodyJson, lastParamType.javaClass)
        } catch (e: IOException) {
            throw RequestPayloadParseException("Unable to parse request body JSON $bodyJson", e)
        }
    }
}