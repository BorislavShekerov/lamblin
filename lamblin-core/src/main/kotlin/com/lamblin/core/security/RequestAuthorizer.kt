package com.lamblin.core.security

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent

/** Defines the mechanism which can be used to authorize requests. */
interface RequestAuthorizer {

    /** Uses the request details to define if the allowed endpoint roles are satisfied. */
    fun isRequestAuthorized(roles: Array<String>, request: APIGatewayProxyRequestEvent): Boolean
}
