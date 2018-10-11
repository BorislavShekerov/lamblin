/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.it.controller

import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.*
import com.lamblin.it.model.*

@Controller
class PutController {

    @Endpoint(SIMPLE_PUT_ENDPOINT, method = HttpMethod.PUT)
    fun simplePostNoParams(): HttpResponse<ResponseEntity> {
        return HttpResponse.ok(ResponseEntity(SIMPLE_PUT_ENDPOINT))
    }

    @Endpoint(QUERY_PARAM_PUT_ENDPOINT, method = HttpMethod.PUT)
    fun singleQueryParamTest(@QueryParam(QUERY_PARAM_1) queryParam: String): HttpResponse<ResponseEntity> {
        return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_PUT_ENDPOINT-$queryParam"))
    }

    @Endpoint(QUERY_PARAM_PUT_ENDPOINT, method = HttpMethod.PUT)
    fun multipleQueryParamTest(
        @QueryParam(QUERY_PARAM_1) queryParam1: String,
        @QueryParam(QUERY_PARAM_2) queryParam2: String
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
            ResponseEntity("$QUERY_PARAM_PUT_ENDPOINT-$queryParam1,$queryParam2"))
    }

    @Endpoint(SINGLE_PATH_PARAM_PUT_ENDPOINT, method = HttpMethod.PUT)
    fun singlePathParamPath(
        @PathParam(PATH_PARAM_1) pathParam: String
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(ResponseEntity("$SINGLE_PATH_PARAM_PUT_ENDPOINT-$pathParam"))
    }

    @Endpoint(SIMPLE_REQUEST_BODY_PUT_ENDPOINT, method = HttpMethod.PUT)
    fun requestBody(
        @RequestBody exampleRequestBody: ExampleRequestBody
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
            ResponseEntity("$SIMPLE_REQUEST_BODY_PUT_ENDPOINT-${exampleRequestBody.body}"))
    }

    @Endpoint(MULTI_PATH_PARAM_PUT_ENDPOINT, method = HttpMethod.PUT)
    fun multiplePathParamPath(
        @PathParam(PATH_PARAM_1) pathParamOne: String,
        @PathParam(PATH_PARAM_2) pathParamTwo: String
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
            ResponseEntity("$MULTI_PATH_PARAM_PUT_ENDPOINT-$pathParamOne,$pathParamTwo"))
    }

    @Endpoint(MULTI_PATH_PARAM_PUT_ENDPOINT, method = HttpMethod.PUT)
    fun multiplePathParamWithQueryParamsPath(
        @QueryParam(QUERY_PARAM_1) queryParam: String,
        @PathParam(PATH_PARAM_1) pathParamOne: String,
        @PathParam(PATH_PARAM_2) pathParamTwo: String
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
            ResponseEntity(
                "$MULTI_PATH_PARAM_PUT_ENDPOINT-$queryParam,$pathParamOne,$pathParamTwo"))
    }

}