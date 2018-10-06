package com.lamblin.it.controller

import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Controller
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import com.lamblin.core.model.annotation.RequestBody
import com.lamblin.it.model.ExampleRequestBody
import com.lamblin.it.model.MULTIPLE_PATH_PARAM_PATH_POST_ENDPOINT
import com.lamblin.it.model.PATH_PARAM_1
import com.lamblin.it.model.PATH_PARAM_2
import com.lamblin.it.model.QUERY_PARAM_1
import com.lamblin.it.model.QUERY_PARAM_2
import com.lamblin.it.model.QUERY_PARAM_PARAM_POST_ENDPOINT
import com.lamblin.it.model.ResponseEntity
import com.lamblin.it.model.SIMPLE_POST_ENDPOINT
import com.lamblin.it.model.SIMPLE_REQUEST_BODY_POST_ENDPOINT
import com.lamblin.it.model.SINGLE_PATH_PARAM_PATH_POST_ENDPOINT

@Controller
class PostController {

    @Endpoint(SIMPLE_POST_ENDPOINT, method = HttpMethod.POST)
    fun simplePostNoParams(): HttpResponse<ResponseEntity> {
        return HttpResponse.ok(ResponseEntity(SIMPLE_POST_ENDPOINT))
    }

    @Endpoint(QUERY_PARAM_PARAM_POST_ENDPOINT, method = HttpMethod.POST)
    fun singleQueryParamTest(@QueryParam(QUERY_PARAM_1) queryParam: String): HttpResponse<ResponseEntity> {
        return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_PARAM_POST_ENDPOINT-$queryParam"))
    }

    @Endpoint(QUERY_PARAM_PARAM_POST_ENDPOINT, method = HttpMethod.POST)
    fun multipleQueryParamTest(
            @QueryParam(QUERY_PARAM_1) queryParam1: String,
            @QueryParam(QUERY_PARAM_2) queryParam2: String): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
                ResponseEntity("$QUERY_PARAM_PARAM_POST_ENDPOINT-$queryParam1,$queryParam2"))
    }

    @Endpoint(SINGLE_PATH_PARAM_PATH_POST_ENDPOINT, method = HttpMethod.POST)
    fun singlePathParamPath(
            @PathParam(PATH_PARAM_1) pathParam: String): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(ResponseEntity("$SINGLE_PATH_PARAM_PATH_POST_ENDPOINT-$pathParam"))
    }

    @Endpoint(MULTIPLE_PATH_PARAM_PATH_POST_ENDPOINT, method = HttpMethod.POST)
    fun multiplePathParamPath(
            @PathParam(PATH_PARAM_1) pathParamOne: String,
            @PathParam(PATH_PARAM_2) pathParamTwo: String): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(ResponseEntity(
                "$MULTIPLE_PATH_PARAM_PATH_POST_ENDPOINT-$pathParamOne,$pathParamTwo"))
    }

    @Endpoint(SIMPLE_REQUEST_BODY_POST_ENDPOINT, method = HttpMethod.POST)
    fun requestBody(
            @RequestBody exampleRequestBody: ExampleRequestBody): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
                ResponseEntity("$SIMPLE_REQUEST_BODY_POST_ENDPOINT-${exampleRequestBody.body}"))
    }

    @Endpoint(MULTIPLE_PATH_PARAM_PATH_POST_ENDPOINT, method = HttpMethod.POST)
    fun multiplePathParamWithQueryParamsPath(
            @QueryParam(QUERY_PARAM_1) queryParam: String,
            @PathParam(PATH_PARAM_1) pathParamOne: String,
            @PathParam(PATH_PARAM_2) pathParamTwo: String): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(ResponseEntity(
                "$MULTIPLE_PATH_PARAM_PATH_POST_ENDPOINT-$queryParam,$pathParamOne,$pathParamTwo"))
    }

}