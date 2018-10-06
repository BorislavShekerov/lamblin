package com.lamblin.local.it

import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Controller
import com.lamblin.core.model.annotation.Endpoint

const val RESPONSE_CONTENT = "test_content"

data class FooResponse(val content: String)

@Controller
class Controller {

    @Endpoint(path = "/test", method = HttpMethod.GET)
    fun test(): HttpResponse<FooResponse> {
        return HttpResponse.ok(FooResponse(RESPONSE_CONTENT))
    }
}
