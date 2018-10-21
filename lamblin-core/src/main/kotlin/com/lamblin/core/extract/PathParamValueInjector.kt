/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.HandlerMethod.Companion.isPathParamSection
import java.lang.reflect.Parameter

/** Defines a mechanism for deciding the values of path parameters (i.e. parameters annotated with [PathParam]). */
internal object PathParamValueInjector : EndpointParamValueInjector {

    override fun injectParamValues(
        request: APIGatewayProxyRequestEvent,
        handlerMethod: HandlerMethod,
        paramAnnotationMappedNameToParam: Map<String, Parameter>
    ): Map<String, Any> {

        val handlerPathSections = handlerMethod.path.split("/")
        val requestPathSections = request.path.split("/")

        return handlerPathSections.asSequence().withIndex()
            .filter { isPathParamSection(it.value) }
            .map { it.value.removeSurrounding("{", "}") to requestPathSections[it.index] }
            .map {
                it.first to castParamToRequiredType(
                    paramAnnotationMappedNameToParam[it.first]!!.type,
                    it.second)
            }.toMap()
    }
}
