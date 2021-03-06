package com.lamblin.it.controller

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.StatusCode
import com.lamblin.core.model.annotation.Controller
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.Header
import com.lamblin.core.security.AccessControl
import com.lamblin.core.security.RequestAuthorizer
import com.lamblin.it.model.*

@Controller
class MiscellaneousController {

    @Endpoint(path = HEADER_GET_ENDPOINT, method = HttpMethod.GET)
    fun headerPath(@Header(AUTHORIZATION_HEADER) authorizationHeader: String): HttpResponse<ResponseEntity> {
        return HttpResponse.ok(
            ResponseEntity("$HEADER_GET_ENDPOINT-$authorizationHeader"))
    }

    @Endpoint(path = API_GATEWAY_REQUEST_EVENT_GET_ENDPOINT, method = HttpMethod.GET)
    fun apiGatewayRequestEventEndpoint(
        apiGatewayRequestEvent: APIGatewayProxyRequestEvent): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
            ResponseEntity("$API_GATEWAY_REQUEST_EVENT_GET_ENDPOINT-${apiGatewayRequestEvent.path}"))
    }

    @Endpoint(path = CUSTOM_STATUS_CODE_GET_ENDPOINT, method = HttpMethod.GET)
    fun customStatusCodeEndpoint(): HttpResponse<Any> {
        return HttpResponse.withCode(StatusCode.ACCEPTED)
    }

    @AccessControl([AUTHORIZED_ROLE], Authorizer::class)
    @Endpoint(path = ACCESS_CONTROL_AUTHORIZED_GET_ENDPOINT, method = HttpMethod.GET)
    fun accessControlAuthorized(): HttpResponse<Void> {
        return HttpResponse.ok()
    }

    @AccessControl([AUTHORIZED_ROLE], Authorizer::class)
    @Endpoint(ACCESS_CONTROL_UNAUTHORIZED_GET_ENDPOINT, HttpMethod.GET)
    fun accessControlUnauthorized(): HttpResponse<Void> {
        return HttpResponse.ok()
    }

    class Authorizer : RequestAuthorizer {

        override fun isRequestAuthorized(roles: Array<String>, request: APIGatewayProxyRequestEvent): Boolean {
            return request.path == ACCESS_CONTROL_AUTHORIZED_GET_ENDPOINT
        }
    }

}