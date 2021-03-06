/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.exception.RequestPayloadParseException
import com.lamblin.core.jsonMapperRegistry
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.REQUEST_BODY_MAPPED_NAME
import com.lamblin.core.model.annotation.RequestBody
import org.slf4j.LoggerFactory
import java.io.IOException
import java.util.Objects.nonNull
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.valueParameters

private val LOGGER = LoggerFactory.getLogger(RequestBodyParamValueInjector::class.java)


/** Defines a mechanism for deciding the values of request body (i.e. parameters annotated with [RequestBody]). */
internal object RequestBodyParamValueInjector : EndpointParamValueInjector {

    private val requestBodyEnabledHttpMethods = setOf(HttpMethod.POST, HttpMethod.PUT, HttpMethod.PATCH)

    override fun injectParamValues(
        request: APIGatewayProxyRequestEvent,
        handlerMethod: HandlerMethod,
        paramAnnotationMappedNameToParam: Map<String, KParameter>
    ) =
        if (canDeserializeRequestBody(handlerMethod, request)) {
            mapOf(deserializeBodyJson(paramAnnotationMappedNameToParam.values, request.body))
        } else {
            mapOf()
        }


    private fun canDeserializeRequestBody(
        handlerMethod: HandlerMethod,
        request: APIGatewayProxyRequestEvent
    ): Boolean {

        return handlerMethod.httpMethod in requestBodyEnabledHttpMethods
                && handlerMethod.method.valueParameters.isNotEmpty()
                && handlerMethod.method.valueParameters.any { parameter -> parameter.annotations.any { it is RequestBody } }
                && request.body.isNotEmpty()
    }

    private fun deserializeBodyJson(parameters: Iterable<KParameter>, bodyJson: String): Pair<String, Any> {
        LOGGER.debug("Attempting to deserialize request body")

        return parameters.find { nonNull(it.findAnnotation<RequestBody>()) }
            .let {
                REQUEST_BODY_MAPPED_NAME to deserializeBody(it!!, bodyJson)
            }
    }

    private fun deserializeBody(parameter: KParameter, bodyJson: String): Any {
        try {
            LOGGER.debug("Deserializing [{}] into [{}]", bodyJson, parameter.type)
            return jsonMapperRegistry.jsonMapper.readValue(bodyJson, (parameter.type.classifier as KClass<*>).javaObjectType)
        } catch (e: IOException) {
            throw RequestPayloadParseException("Unable to parse request body JSON $bodyJson", e)
        }
    }
}
