/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod
import org.slf4j.LoggerFactory
import kotlin.reflect.KParameter
import kotlin.reflect.full.valueParameters

private val LOGGER = LoggerFactory.getLogger(CompositeParamValueInjector::class.java)

/**
 * Defines the mechanism which assigns a value to each parameter of the [HandlerMethod]
 * using the details in the [APIGatewayProxyRequestEvent] for the request being served.
 */
internal class CompositeParamValueInjector internal constructor(
    internal val injectorEndpoints: List<EndpointParamValueInjector>) : EndpointParamValueInjector {

    companion object {
        fun instance(): CompositeParamValueInjector = CompositeParamValueInjector(
            listOf(
                PathParamValueInjector,
                QueryParamValueInjector,
                RequestBodyParamValueInjector,
                HeaderParamValueInjector,
                ApiGatewayRequestParamValueInjector))
    }

    override fun injectParamValues(
        request: APIGatewayProxyRequestEvent,
        handlerMethod: HandlerMethod,
        paramAnnotationMappedNameToParam: Map<String, KParameter>): Map<String, Any?> {

        LOGGER.debug("Extracting param values from request ${request.path}")

        val paramAnnotationMappedNameToValue = injectorEndpoints.map {
            it.injectParamValues(request, handlerMethod, paramAnnotationMappedNameToParam)
        }.reduceRight { a, b -> a + b }

        val result = linkedMapOf<String, Any?>()

        return handlerMethod.method.valueParameters.asSequence()
            .map {
                handlerMethod.paramNameToParam[it.name]
                        ?: throw IllegalStateException("Param not found for ${it.name}")
            }
            .map { it.annotationMappedName to paramAnnotationMappedNameToValue[it.annotationMappedName] }
            .toMap(result)
    }

}
