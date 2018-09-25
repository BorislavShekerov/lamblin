package com.lamblin.core

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HandlerMethod.Companion.isPathParamSection
import com.lamblin.core.model.HandlerMethodParameter
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.annotation.RequestBody
import org.slf4j.LoggerFactory
import kotlin.reflect.KParameter

private val LOGGER = LoggerFactory.getLogger(EndpointInvoker::class.java)

internal class RequestToParamValueMapper private constructor(
        private val bodyJsonToParamDeserializer: BodyJsonToParamDeserializer) {

    companion object {
        fun instance() = RequestToParamValueMapper(DefaultBodyJsonToParamDeserializer)
    }

    // Creates a list of param objects to be passed in when invoking the method
    internal fun mapRequestParamsToValues(
            request: APIGatewayProxyRequestEvent,
            handlerMethod: HandlerMethod): Array<Any> {

        val parameters = handlerMethod.method.parameters

        LOGGER.debug("Query string parameters passed in [{}]", request.queryStringParameters)

        val queryAndPathParams = combineQueryAndPathParams(request, handlerMethod.path)
        val paramToValue = if (HttpMethod.POST !== handlerMethod.httpMethod)
            queryAndPathParams
        else
            addRequestBodyToParams(request, parameters, queryAndPathParams)

        return parameters
                .map {
                    handlerMethod.paramNameToParam[it.name]
                            ?.annotationMappedName
                            ?: throw java.lang.IllegalStateException("Param name not found for param ${it.name}")
                }
                .map { getValueForParam(it, handlerMethod.paramNameToParam, paramToValue) }
                .toTypedArray()
    }

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
            paramMappedName: String,
            paramNameToParam: Map<String, HandlerMethodParameter>,
            paramToValue: Map<String, Any>): Any {

        val handlerMethodParameter = paramNameToParam.values
                .find { paramMappedName == it.annotationMappedName }
                ?: throw IllegalStateException("Handler param not found for [ $paramMappedName ]")

        return if (paramToValue.containsKey(paramMappedName)) {
            castParamToRequiredType(handlerMethodParameter.type, paramToValue[paramMappedName]!!)
        } else castParamToRequiredType(handlerMethodParameter.type, handlerMethodParameter.defaultValue ?: "")

    }

    private fun castParamToRequiredType(paramType: Class<*>, paramValue: Any) = when (paramType) {
        Int::class.java, Long::class.java -> paramValue as String
        Int::class.java -> paramValue as String
        else -> paramValue
    }

    // Attempts to deserialize the body JSON into an object, if the request is POST and request body is present
    private fun addRequestBodyToParams(
            request: APIGatewayProxyRequestEvent,
            parameters: List<KParameter>,
            paramToValue: Map<String, Any>): Map<String, Any> =

            if (HttpMethod.POST.name == request.httpMethod && parameters.isNotEmpty() && request.body.isNotEmpty()) {
                LOGGER.debug("Attempting to deserialize request body")

                val requestBodyParameter = parameters.find {
                    it.annotations.any { annotation -> annotation is RequestBody }
                }

                paramToValue + (requestBodyParameter?.let {
                    mapOf(it.name!! to bodyJsonToParamDeserializer.deserializeBodyJsonForParameter(it, request.body))
                } ?: mapOf())
            } else paramToValue
}
