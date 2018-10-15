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

/** Defines the mechanism for injecting the values into the endpoint params, based on the request details. */
internal interface EndpointParamValueInjector {

    /**
     * Computes the [HandlerMethod] param values using the details of the [APIGatewayProxyRequestEvent].
     * Returns an ordered paramAnnotationMappedName => paramValue map.
     */
    fun injectParamValues(
        request: APIGatewayProxyRequestEvent,
        handlerMethod: HandlerMethod,
        paramAnnotationMappedNameToParam: Map<String, Parameter>
    ): Map<String, Any>

    fun castParamToRequiredType(paramType: Class<*>?, paramValue: Any) = when (paramType) {
        Int::class.java -> (paramValue as String).toInt()
        Long::class.java -> (paramValue as String).toLong()
        Double::class.java -> (paramValue as String).toDouble()
        Boolean::class.java -> (paramValue as String).toBoolean()
        else -> paramValue
    }
}

