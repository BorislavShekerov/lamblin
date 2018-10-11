/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod
import org.slf4j.LoggerFactory
import java.lang.reflect.Parameter

private val LOGGER = LoggerFactory.getLogger(QueryEndpointParamValueInjector::class.java)

/** Defines a mechanism for deciding the values of query parameters (i.e. parameters annotated with [QueryParam]). */
object QueryEndpointParamValueInjector : EndpointParamValueInjector {

    override fun injectParamValues(
        request: APIGatewayProxyRequestEvent,
        handlerMethod: HandlerMethod,
        paramAnnotationMappedNameToParam: Map<String, Parameter>
    ): Map<String, Any> {

        LOGGER.debug("Extracting query param values from request ${request.path}")

        return request.queryStringParameters
            ?.mapValues { castParamToRequiredType(paramAnnotationMappedNameToParam[it.key]?.type, it.value) }
                ?: mapOf()
    }
}
