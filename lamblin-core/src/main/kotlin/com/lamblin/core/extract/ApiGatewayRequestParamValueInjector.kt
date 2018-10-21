package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod
import java.lang.reflect.Parameter

/** Defines the mechanism for injecting [APIGatewayProxyRequestEvent] value in the handler method param. */
internal object ApiGatewayRequestParamValueInjector: EndpointParamValueInjector {

    /** Injects the [APIGatewayProxyRequestEvent] in the handler method param, if such a param is present. */
    override fun injectParamValues(
        request: APIGatewayProxyRequestEvent,
        handlerMethod: HandlerMethod,
        paramAnnotationMappedNameToParam: Map<String, Parameter>): Map<String, Any?> {

        return paramAnnotationMappedNameToParam.entries
            .find { it.value.type == APIGatewayProxyRequestEvent::class.java }
            ?.let { mapOf(it.key to request) }
            ?: mapOf()
    }
}
