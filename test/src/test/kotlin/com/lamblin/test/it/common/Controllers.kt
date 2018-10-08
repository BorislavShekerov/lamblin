package com.lamblin.test.it.common

import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Controller
import com.lamblin.core.model.annotation.Endpoint

data class TestResponse(val content: String)

const val CONTROLLER1_RESPONSE_CONTENT = "controller1_response"
const val CONTROLLER2_RESPONSE_CONTENT = "controller2_response"

@Controller
class Controller1 {

    @Endpoint("/controller1-endpoint", method = HttpMethod.GET)
    fun controller1Endpoint(): HttpResponse<TestResponse> {
        return HttpResponse.ok(TestResponse(CONTROLLER1_RESPONSE_CONTENT))
    }
}

@Controller
class Controller2 {

    @Endpoint("/controller2-endpoint", method = HttpMethod.GET)
    fun controller1Endpoint(): HttpResponse<TestResponse> {
        return HttpResponse.ok(TestResponse(CONTROLLER2_RESPONSE_CONTENT))
    }
}
