package it.kotlin.controller

import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.StatusCode
import com.lamblin.core.model.annotation.Endpoint
import com.lamblin.core.model.annotation.PathParam
import com.lamblin.core.model.annotation.QueryParam
import it.PATH_PARAM_1
import it.PATH_PARAM_2
import it.QUERY_PARAM_1
import it.QUERY_PARAM_2
import it.ResponseEntity
import it.kotlin.CUSTOM_STATUS_CODE_GET_ENDPOINT
import it.kotlin.MULTIPLE_PATH_PARAM_GET_ENDPOINT
import it.kotlin.QUERY_PARAM_GET_ENDPOINT
import it.kotlin.SIMPLE_GET_ENDPOINT
import it.kotlin.SINGLE_PATH_PARAM_GET_ENDPOINT

class GetController {

    @Endpoint(SIMPLE_GET_ENDPOINT, method = HttpMethod.GET)
    fun simpleGetNoParams(): HttpResponse<ResponseEntity> {
        return HttpResponse.ok(ResponseEntity(SIMPLE_GET_ENDPOINT))
    }

    @Endpoint(QUERY_PARAM_GET_ENDPOINT, method = HttpMethod.GET)
    fun signleQueryParamTest(@QueryParam(QUERY_PARAM_1) queryParam: String): HttpResponse<ResponseEntity> {
        return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_GET_ENDPOINT-$queryParam"))
    }

    @Endpoint(QUERY_PARAM_GET_ENDPOINT, method = HttpMethod.GET)
    fun multipleQueryParamTest(
            @QueryParam(QUERY_PARAM_1) queryParam1: String,
            @QueryParam(QUERY_PARAM_2) queryParam2: String): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(ResponseEntity("$QUERY_PARAM_GET_ENDPOINT-$queryParam1,$queryParam2"))
    }

    @Endpoint(SINGLE_PATH_PARAM_GET_ENDPOINT, method = HttpMethod.GET)
    fun singlePathParamPath(
            @PathParam(PATH_PARAM_1) pathParam: String): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(ResponseEntity("$SINGLE_PATH_PARAM_GET_ENDPOINT-$pathParam"))
    }

    @Endpoint(MULTIPLE_PATH_PARAM_GET_ENDPOINT, method = HttpMethod.GET)
    fun multiplePathParamPath(
            @PathParam(PATH_PARAM_1) pathParamOne: String,
            @PathParam(PATH_PARAM_2) pathParamTwo: String): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(
                ResponseEntity("$MULTIPLE_PATH_PARAM_GET_ENDPOINT-$pathParamOne,$pathParamTwo"))
    }

    @Endpoint(MULTIPLE_PATH_PARAM_GET_ENDPOINT, method = HttpMethod.GET)
    fun multiplePathParamWithQueryParamsPath(
            @QueryParam(QUERY_PARAM_1) queryParam: String,
            @PathParam(PATH_PARAM_1) pathParamOne: String,
            @PathParam(PATH_PARAM_2) pathParamTwo: String): HttpResponse<ResponseEntity> {

        return HttpResponse.ok(ResponseEntity(
                "$MULTIPLE_PATH_PARAM_GET_ENDPOINT-$queryParam,$pathParamOne,$pathParamTwo"))
    }

    @Endpoint(CUSTOM_STATUS_CODE_GET_ENDPOINT, method = HttpMethod.GET)
    fun customStatusCodeEndpoint(): HttpResponse<ResponseEntity> {
        return HttpResponse(statusCode = StatusCode.ACCEPTED)
    }

}