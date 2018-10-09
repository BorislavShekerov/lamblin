package com.lamblin.it.controller

import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.Controller
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import com.lamblin.it.model.*

@Controller
class DeleteController {

    @Endpoint(SIMPLE_DELETE_ENDPOINT, method = HttpMethod.DELETE)
    fun simpleDeleteNoParams(): HttpResponse<ResponseEntity> {
        return HttpResponse.ok(ResponseEntity(SIMPLE_DELETE_ENDPOINT))
    }

    @Endpoint(QUERY_PARAM_DELETE_ENDPOINT, method = HttpMethod.DELETE)
    fun signleQueryParamTest(@QueryParam(QUERY_PARAM_1) queryParam: String): HttpResponse<ResponseEntity> {
        return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_DELETE_ENDPOINT-$queryParam"))
    }

    @Endpoint(QUERY_PARAM_DELETE_ENDPOINT, method = HttpMethod.DELETE)
    fun multipleQueryParamTest(
        @QueryParam(QUERY_PARAM_1) queryParam1: String,
        @QueryParam(QUERY_PARAM_2) queryParam2: String
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_DELETE_ENDPOINT-$queryParam1,$queryParam2"))
    }

    @Endpoint(SINGLE_PATH_PARAM_DELETE_ENDPOINT, method = HttpMethod.DELETE)
    fun singlePathParamPath(
        @PathParam(PATH_PARAM_1) pathParam: String
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(ResponseEntity("$SINGLE_PATH_PARAM_DELETE_ENDPOINT-$pathParam"))
    }

    @Endpoint(MULTIPLE_PATH_PARAM_DELETE_ENDPOINT, method = HttpMethod.DELETE)
    fun multiplePathParamPath(
        @PathParam(PATH_PARAM_1) pathParamOne: String,
        @PathParam(PATH_PARAM_2) pathParamTwo: String
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
            ResponseEntity("$MULTIPLE_PATH_PARAM_DELETE_ENDPOINT-$pathParamOne,$pathParamTwo"))
    }

    @Endpoint(MULTIPLE_PATH_PARAM_DELETE_ENDPOINT, method = HttpMethod.DELETE)
    fun multiplePathParamWithQueryParamsPath(
        @QueryParam(QUERY_PARAM_1) queryParam: String,
        @PathParam(PATH_PARAM_1) pathParamOne: String,
        @PathParam(PATH_PARAM_2) pathParamTwo: String
    ): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
            ResponseEntity(
                "$MULTIPLE_PATH_PARAM_DELETE_ENDPOINT-$queryParam,$pathParamOne,$pathParamTwo"))
    }

}