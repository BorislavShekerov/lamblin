/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.it.controller

import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.StatusCode
import com.lamblin.core.model.annotation.Controller
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import com.lamblin.it.model.*

@Controller
class GetController {

    @Endpoint(SIMPLE_GET_ENDPOINT, method = HttpMethod.GET)
    fun simpleGetNoParamsPath(): HttpResponse<ResponseEntity> {
        return HttpResponse.ok(ResponseEntity(SIMPLE_GET_ENDPOINT))
    }

    @Endpoint(QUERY_PARAM_GET_ENDPOINT, method = HttpMethod.GET)
    fun signleQueryParamPath(@QueryParam(QUERY_PARAM_1) queryParam: String): HttpResponse<ResponseEntity> {
        return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_GET_ENDPOINT-$queryParam"))
    }

    @Endpoint(QUERY_PARAM_DEFAULT_VALUE_GET_ENDPOINT, method = HttpMethod.GET)
    fun queryParamDefaultValuePath(@QueryParam(QUERY_PARAM_1, defaultValue = DEFAULT_QUERY_PARAM_VALUE) queryParam: String): HttpResponse<ResponseEntity> {
        return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_DEFAULT_VALUE_GET_ENDPOINT-$queryParam"))
    }

    @Endpoint(QUERY_PARAM_GET_ENDPOINT, method = HttpMethod.GET)
    fun multipleQueryParamTest(
        @QueryParam(QUERY_PARAM_1) queryParam1: String,
        @QueryParam(QUERY_PARAM_2) queryParam2: String): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_GET_ENDPOINT-$queryParam1,$queryParam2"))
    }

    @Endpoint(QUERY_PARAM_MULTI_KEY_GET_ENDPOINT, method = HttpMethod.GET)
    fun multipleQueryParamTest(
        @QueryParam(QUERY_PARAM_1) queryParamValues: Array<String>): HttpResponse<ResponseEntity> {

        if (queryParamValues.size == 1) {
            return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_MULTI_KEY_GET_ENDPOINT-${queryParamValues[0]}"))
        }

        return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_MULTI_KEY_GET_ENDPOINT-${queryParamValues[0]},${queryParamValues[1]}"))
    }


    @Endpoint(SINGLE_PATH_PARAM_GET_ENDPOINT, method = HttpMethod.GET)
    fun singlePathParamPath(
        @PathParam(PATH_PARAM_1) pathParam: String
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(ResponseEntity("$SINGLE_PATH_PARAM_GET_ENDPOINT-$pathParam"))
    }

    @Endpoint(MULTI_PATH_PARAM_GET_ENDPOINT, method = HttpMethod.GET)
    fun multiplePathParamPath(
        @PathParam(PATH_PARAM_1) pathParamOne: String,
        @PathParam(PATH_PARAM_2) pathParamTwo: String
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
            ResponseEntity("$MULTI_PATH_PARAM_GET_ENDPOINT-$pathParamOne,$pathParamTwo"))
    }

    @Endpoint(MULTI_PATH_PARAM_GET_ENDPOINT, method = HttpMethod.GET)
    fun multiplePathParamWithQueryParamsPath(
        @QueryParam(QUERY_PARAM_1) queryParam: String,
        @PathParam(PATH_PARAM_1) pathParamOne: String,
        @PathParam(PATH_PARAM_2) pathParamTwo: String
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
            ResponseEntity(
                "$MULTI_PATH_PARAM_GET_ENDPOINT-$queryParam,$pathParamOne,$pathParamTwo"))
    }

    @Endpoint(CUSTOM_STATUS_CODE_GET_ENDPOINT, method = HttpMethod.GET)
    fun customStatusCodeEndpoint(): HttpResponse<ResponseEntity> {
        return HttpResponse(statusCode = StatusCode.ACCEPTED)
    }

}