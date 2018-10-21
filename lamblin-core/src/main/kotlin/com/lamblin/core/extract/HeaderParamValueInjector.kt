package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.annotation.Header
import java.lang.reflect.Parameter

/** Defines a mechanism for deciding the values of header parameters (i.e. parameters annotated with [Header]). */
internal object HeaderParamValueInjector : EndpointParamValueInjector {

    override fun injectParamValues(
        request: APIGatewayProxyRequestEvent,
        handlerMethod: HandlerMethod,
        paramAnnotationMappedNameToParam: Map<String, Parameter>): Map<String, Any?> {

        val headerParams = paramAnnotationMappedNameToParam.values
            .filter { it.isAnnotationPresent(Header::class.java) }

        return headerParams.map { it.getAnnotation(Header::class.java) }
            .map { it.value to request?.headers?.get(it.value) }
            .toMap()

    }
}
