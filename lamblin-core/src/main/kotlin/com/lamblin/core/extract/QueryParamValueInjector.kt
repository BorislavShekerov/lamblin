/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.annotation.QueryParam
import org.slf4j.LoggerFactory
import java.util.Objects.nonNull
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

private val LOGGER = LoggerFactory.getLogger(QueryParamValueInjector::class.java)

/** Defines a mechanism for extracting the values of query parameters. */
internal object QueryParamValueInjector : EndpointParamValueInjector {

    override fun injectParamValues(
        request: APIGatewayProxyRequestEvent,
        handlerMethod: HandlerMethod,
        paramAnnotationMappedNameToParam: Map<String, KParameter>): Map<String, Any?> {

        LOGGER.debug("Extracting query param values from request ${request.path}")

        val queryParamAnnotatedParameters = paramAnnotationMappedNameToParam.values
            .filter { nonNull(it.findAnnotation<QueryParam>()) }

        return queryParamAnnotatedParameters.map { it.findAnnotation<QueryParam>() }
            .map {
                it!!.value to computeQueryParamValue(
                    request.multiValueQueryStringParameters,
                    it.value,
                    it.defaultValue,
                    paramAnnotationMappedNameToParam[it.value]!!)
            }
            .toMap()
    }

    private fun computeQueryParamValue(
        requestQueryStringParameters: Map<String, List<String>>?,
        paramName: String,
        paramDefaultValue: String,
        parameter: KParameter) =

        if (requestQueryStringParameters?.containsKey(paramName) == true) {
            castParamToRequiredType(
                parameter.type.classifier as KClass<*>,
                requestQueryStringParameters[paramName]!!.toTypedArray())
        } else {
            if (paramDefaultValue.isNotBlank()) {
                castParamToRequiredType(parameter.type.classifier as KClass<*>, paramDefaultValue)
            } else null
        }
}
