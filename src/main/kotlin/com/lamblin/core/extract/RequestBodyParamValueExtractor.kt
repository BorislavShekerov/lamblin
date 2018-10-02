package com.lamblin.core.extract

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HandlerMethod

class RequestBodyParamValueExtractor internal constructor(
        val bodyJsonDeserializer: BodyJsonDeserializer) : ParamValueExtractor {

    override fun extractParamValuesFromRequest(request: APIGatewayProxyRequestEvent,
                                               handlerMethod: HandlerMethod): Array<Any> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}