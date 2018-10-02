package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod

object QueryParamValueExtractor : ParamValueExtractor {

    override fun extractParamValuesFromRequest(
            request: APIGatewayProxyRequestEvent,
            handlerMethod: HandlerMethod): Map<String, Any> {

        return request.queryStringParameters.toMap()
    }
}