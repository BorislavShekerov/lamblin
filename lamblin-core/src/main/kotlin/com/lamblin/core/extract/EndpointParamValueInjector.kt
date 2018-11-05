/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod
import kotlin.reflect.KClass
import kotlin.reflect.KParameter

/** Defines the mechanism for injecting the values into the endpoint params, based on the request details. */
internal interface EndpointParamValueInjector {

    /**
     * Computes the [HandlerMethod] param values using the details of the [APIGatewayProxyRequestEvent].
     * Returns an ordered paramAnnotationMappedName => paramValue map.
     */
    fun injectParamValues(
        request: APIGatewayProxyRequestEvent,
        handlerMethod: HandlerMethod,
        paramAnnotationMappedNameToParam: Map<String, KParameter>
    ): Map<String, Any?>

    fun castParamToRequiredType(paramType: KClass<*>?, paramValue: Any) = when (paramType) {
        Int::class -> (paramValue as String).toInt()
        Long::class -> (paramValue as String).toLong()
        Double::class -> (paramValue as String).toDouble()
        Boolean::class -> (paramValue as String).toBoolean()
        else -> paramValue
    }
}

