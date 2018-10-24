package core.security

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lamblin.core.security.AccessControl
import com.lamblin.core.security.DefaultEndpointAuthorizationChecker
import com.lamblin.core.security.RequestAuthorizer
import org.junit.jupiter.api.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DefaultEndpointAuthorizationCheckerTest {

    @Test
    fun `should use authorizer in @AccessControl to check if request authorized, authorized expected`() {
        val isAuthorized = DefaultEndpointAuthorizationChecker.isRequestAuthorized(
            APIGatewayProxyRequestEvent(),
            TestController::class.java.methods.find { it.name == "testEndpointWithPassingAccessControl"}!!.annotations[0] as AccessControl)

        assertTrue { isAuthorized }
    }

    @Test
    fun `should use singleton authorizer in @AccessControl to check if request authorized, authorized expected`() {
        val isAuthorized = DefaultEndpointAuthorizationChecker.isRequestAuthorized(
            APIGatewayProxyRequestEvent(),
            TestController::class.java.methods.find { it.name == "testEndpointWithPassingAccessControlAuthorizerSingleton"}!!.annotations[0] as AccessControl)

        assertTrue { isAuthorized }
    }

    @Test
    fun `should use authorizer in @AccessControl to check if request authorized, unauthorized expected`() {
        val isAuthorized = DefaultEndpointAuthorizationChecker.isRequestAuthorized(
            APIGatewayProxyRequestEvent(),
            TestController::class.java.methods.find { it.name == "testEndpointWithFailingAccessControl"}!!.annotations[0] as AccessControl)

        assertFalse { isAuthorized }
    }

    private class TestController {

        @AccessControl(["user"], AuthorizorClass::class)
        fun testEndpointWithPassingAccessControl() {
        }

        @AccessControl(["user"], AuthorizorSingleton::class)
        fun testEndpointWithPassingAccessControlAuthorizerSingleton() {
        }

        @AccessControl(["guest"], AuthorizorClass::class)
        fun testEndpointWithFailingAccessControl() {
        }
    }

    object AuthorizorSingleton: RequestAuthorizer {

        override fun isRequestAuthorized(roles: Array<String>, request: APIGatewayProxyRequestEvent): Boolean {
            return "user" in roles
        }
    }

    class AuthorizorClass: RequestAuthorizer {

        override fun isRequestAuthorized(roles: Array<String>, request: APIGatewayProxyRequestEvent): Boolean {
            return "user" in roles
        }
    }
}
