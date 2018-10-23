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
    fun `should use authorizer in @AccessControl to check if request authorized, authorized`() {
        val isAuthorized = DefaultEndpointAuthorizationChecker.isRequestAuthorized(
            APIGatewayProxyRequestEvent(),
            TestController::class.java.methods.find { it.name == "testEndpointWithPassingAccessControl"}!!.annotations[0] as AccessControl)

        assertTrue { isAuthorized }
    }

    @Test
    fun `should use authorizer in @AccessControl to check if request authorized, unauthorized`() {
        val isAuthorized = DefaultEndpointAuthorizationChecker.isRequestAuthorized(
            APIGatewayProxyRequestEvent(),
            TestController::class.java.methods.find { it.name == "testEndpointWithFailingAccessControl"}!!.annotations[0] as AccessControl)

        assertFalse { isAuthorized }
    }

    private class TestController {

        @AccessControl(["user"], Authorizor::class)
        fun testEndpointWithPassingAccessControl() {
        }

        @AccessControl(["guest"], Authorizor::class)
        fun testEndpointWithFailingAccessControl() {
        }
    }

    class Authorizor: RequestAuthorizer {

        override fun isRequestAuthorized(roles: Array<String>, request: APIGatewayProxyRequestEvent): Boolean {
            return "user" in roles
        }
    }
}
