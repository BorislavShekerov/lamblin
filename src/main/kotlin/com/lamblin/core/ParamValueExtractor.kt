package com.lamblin.core

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HandlerMethod.Companion.isPathParamSection
import com.lamblin.core.model.HandlerMethodParameter
import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger(EndpointInvoker::class.java)

/**
 * Defines the mechanism which assigns a value to each parameter of the [HandlerMethod]
 * using the details in the [APIGatewayProxyRequestEvent] for the request being served.
 */
internal class ParamValueExtractor private constructor(
        private val bodyJsonDeserializer: BodyJsonDeserializer) {

    companion object {
        fun instance() = ParamValueExtractor(DefaultBodyJsonDeserializer)
    }

    // Creates a list of param objects to be passed in when invoking the method
    internal fun extractParamValuesFromRequest(
            request: APIGatewayProxyRequestEvent,
            handlerMethod: HandlerMethod): Array<Any> {

        val parameters = handlerMethod.method.parameters

        LOGGER.debug("Query string parameters passed in [{}]", request.queryStringParameters)

        val queryAndPathParamNameToValue = combineQueryAndPathParams(request, handlerMethod.path)
        val paramNameToValue = if (bodyJsonDeserializer.canDeserializeRequestBody(handlerMethod, request))
                queryAndPathParamNameToValue + bodyJsonDeserializer.deserializeBodyJson(handlerMethod.method.parameters, request.body)
        else
            queryAndPathParamNameToValue

        return parameters
                .map {
                    handlerMethod.paramNameToParam[it.name]
                            ?: throw IllegalStateException("Param name not found for param ${it.name}")
                }
                .map { getValueForParam(it, paramNameToValue) }
                .toTypedArray()
    }

    // Creates a combined map of all parameters(query and path) to their values
    private fun combineQueryAndPathParams(
            request: APIGatewayProxyRequestEvent,
            handlerMethodPath: String): Map<String, Any> {

        val handlerPathSections = handlerMethodPath.split("/")
        val requestPathSections = request.path.split("/")

        val queryParamToValue = request.queryStringParameters.toMap()

        val pathParamToValue = handlerPathSections.asSequence().withIndex()
                .filter { isPathParamSection(it.value) }
                .map { it.value to requestPathSections[it.index] }
                .toMap()

        return queryParamToValue + pathParamToValue
    }

    // Gets the param value looking at the path and query params
    // if none of the matches the target param, a check for default value is done
    private fun getValueForParam(
            handlerMethodParameter: HandlerMethodParameter,
            paramNameToValue: Map<String, Any>): Any {

        return castParamToRequiredType(
                handlerMethodParameter.type,
                paramNameToValue[handlerMethodParameter.annotationMappedName]
                        ?: handlerMethodParameter.defaultValue
                        ?: "")
    }

    private fun castParamToRequiredType(paramType: Class<*>, paramValue: Any) = when (paramType) {
        Int::class.java -> (paramValue as String).toInt()
        Long::class.java -> (paramValue as String).toLong()
        Boolean::class.java -> (paramValue as String).toBoolean()
        else -> paramValue
    }

}
