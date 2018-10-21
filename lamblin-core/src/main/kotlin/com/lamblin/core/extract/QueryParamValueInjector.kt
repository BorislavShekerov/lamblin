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
import java.lang.reflect.Parameter

private val LOGGER = LoggerFactory.getLogger(QueryParamValueInjector::class.java)

/** Defines a mechanismQueryEndpointParamValueInjector for deciding the values of query parameters. */
internal object QueryParamValueInjector : EndpointParamValueInjector {

    override fun injectParamValues(
            request: APIGatewayProxyRequestEvent,
            handlerMethod: HandlerMethod,
            paramAnnotationMappedNameToParam: Map<String, Parameter>): Map<String, Any?> {

        LOGGER.debug("Extracting query param values from request ${request.path}")

        val queryParamAnnotatedParameters = paramAnnotationMappedNameToParam.values
                .filter { it.isAnnotationPresent(QueryParam::class.java) }

        return queryParamAnnotatedParameters.map { it.getAnnotation(QueryParam::class.java) }
                .map { it.value to computeQueryParamValue(request.queryStringParameters, it.value, it.defaultValue) }
                .toMap()
    }

    private fun computeQueryParamValue(
            queryStringParameters: Map<String, String>?,
            queryParamName: String,
            defaultValue: String) =

            if (queryStringParameters?.containsKey(queryParamName) == true) {
                queryStringParameters[queryParamName]
            } else {
                if (defaultValue.isNotBlank()) {
                    defaultValue
                } else null
            }
}
