package com.lamblin.core.security

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import kotlin.reflect.full.createInstance

/** Defines the mechanism for checking request access control. */
internal interface EndpointAuthorizationChecker {

    /** Checks if the request is authorized. */
    fun isRequestAuthorized(request: APIGatewayProxyRequestEvent, accessControl: AccessControl): Boolean
}

object DefaultEndpointAuthorizationChecker: EndpointAuthorizationChecker {

    override fun isRequestAuthorized(request: APIGatewayProxyRequestEvent, accessControl: AccessControl): Boolean {
        val requestAuthorizer = accessControl.authorizer.createInstance()

        return requestAuthorizer.isRequestAuthorized(accessControl.roles, request)
    }
}
