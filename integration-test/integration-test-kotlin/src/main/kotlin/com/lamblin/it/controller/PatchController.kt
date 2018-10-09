package com.lamblin.it.controller

import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Controller
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import com.lamblin.core.model.annotation.RequestBody
import com.lamblin.it.model.ExampleRequestBody
import com.lamblin.it.model.MULTI_PATH_PARAM_PATCH_ENDPOINT
import com.lamblin.it.model.PATH_PARAM_1
import com.lamblin.it.model.PATH_PARAM_2
import com.lamblin.it.model.QUERY_PARAM_1
import com.lamblin.it.model.QUERY_PARAM_2
import com.lamblin.it.model.QUERY_PARAM_PATCH_ENDPOINT
import com.lamblin.it.model.ResponseEntity
import com.lamblin.it.model.SIMPLE_PATCH_ENDPOINT
import com.lamblin.it.model.SIMPLE_REQUEST_BODY_PATCH_ENDPOINT
import com.lamblin.it.model.SINGLE_PATH_PARAM_PATCH_ENDPOINT

@Controller
class PatchController {

    @Endpoint(SIMPLE_PATCH_ENDPOINT, method = HttpMethod.PATCH)
    fun simplePostNoParams(): HttpResponse<ResponseEntity> {
        return HttpResponse.ok(ResponseEntity(SIMPLE_PATCH_ENDPOINT))
    }

    @Endpoint(QUERY_PARAM_PATCH_ENDPOINT, method = HttpMethod.PATCH)
    fun singleQueryParamTest(@QueryParam(QUERY_PARAM_1) queryParam: String): HttpResponse<ResponseEntity> {
        return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_PATCH_ENDPOINT-$queryParam"))
    }

    @Endpoint(QUERY_PARAM_PATCH_ENDPOINT, method = HttpMethod.PATCH)
    fun multipleQueryParamTest(
        @QueryParam(QUERY_PARAM_1) queryParam1: String,
        @QueryParam(QUERY_PARAM_2) queryParam2: String
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
            ResponseEntity("$QUERY_PARAM_PATCH_ENDPOINT-$queryParam1,$queryParam2"))
    }

    @Endpoint(SINGLE_PATH_PARAM_PATCH_ENDPOINT, method = HttpMethod.PATCH)
    fun singlePathParamPath(
        @PathParam(PATH_PARAM_1) pathParam: String
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(ResponseEntity("$SINGLE_PATH_PARAM_PATCH_ENDPOINT-$pathParam"))
    }

    @Endpoint(MULTI_PATH_PARAM_PATCH_ENDPOINT, method = HttpMethod.PATCH)
    fun multiplePathParamPath(
        @PathParam(PATH_PARAM_1) pathParamOne: String,
        @PathParam(PATH_PARAM_2) pathParamTwo: String
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
            ResponseEntity(
                "$MULTI_PATH_PARAM_PATCH_ENDPOINT-$pathParamOne,$pathParamTwo"))
    }

    @Endpoint(SIMPLE_REQUEST_BODY_PATCH_ENDPOINT, method = HttpMethod.PATCH)
    fun requestBody(
        @RequestBody exampleRequestBody: ExampleRequestBody
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
            ResponseEntity("$SIMPLE_REQUEST_BODY_PATCH_ENDPOINT-${exampleRequestBody.body}"))
    }

    @Endpoint(MULTI_PATH_PARAM_PATCH_ENDPOINT, method = HttpMethod.PATCH)
    fun multiplePathParamWithQueryParamsPath(
        @QueryParam(QUERY_PARAM_1) queryParam: String,
        @PathParam(PATH_PARAM_1) pathParamOne: String,
        @PathParam(PATH_PARAM_2) pathParamTwo: String
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
            ResponseEntity(
                "$MULTI_PATH_PARAM_PATCH_ENDPOINT-$queryParam,$pathParamOne,$pathParamTwo"))
    }

}