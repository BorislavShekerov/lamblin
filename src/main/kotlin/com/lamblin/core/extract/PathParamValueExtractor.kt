package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod.Companion.isPathParamSection
import java.lang.reflect.Parameter

object PathParamValueExtractor: ParamValueExtractor {

    override fun extractParamValuesFromRequest(request: APIGatewayProxyRequestEvent,
                                               methodPath: String,
                                               paramAnnotationMappedNameToParam: Map<String, Parameter>): Map<String, Any> {

        val handlerPathSections = methodPath.split("/")
        val requestPathSections = request.path.split("/")

        return handlerPathSections.asSequence().withIndex()
                .filter { isPathParamSection(it.value) }
                .map { it.value to requestPathSections[it.index] }
                .map { paramAnnotationMappedNameToParam[it] to "" }
                .toMap()
    }
}