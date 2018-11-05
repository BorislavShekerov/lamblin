package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod
import com.lamblin.core.model.annotation.Header
import java.util.Objects.nonNull
import kotlin.reflect.KParameter
import kotlin.reflect.full.findAnnotation

/** Defines a mechanism for deciding the values of header parameters (i.e. parameters annotated with [Header]). */
internal object HeaderParamValueInjector : EndpointParamValueInjector {

    override fun injectParamValues(
        request: APIGatewayProxyRequestEvent,
        handlerMethod: HandlerMethod,
        paramAnnotationMappedNameToParam: Map<String, KParameter>): Map<String, Any?> {

        val headerParams = paramAnnotationMappedNameToParam.values
            .filter { nonNull(it.findAnnotation<Header>()) }

        return headerParams.map { it.findAnnotation<Header>() }
            .map { it!!.value to request?.headers?.get(it.value) }
            .toMap()

    }
}
