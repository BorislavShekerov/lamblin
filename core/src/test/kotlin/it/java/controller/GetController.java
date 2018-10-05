package it.java.controller;

import com.lamblin.core.model.HttpMethod;
import com.lamblin.core.model.HttpResponse;
import com.lamblin.core.model.StatusCode;
import com.lamblin.core.model.annotation.Endpoint;
import com.lamblin.core.model.annotation.PathParam;
import com.lamblin.core.model.annotation.QueryParam;

import it.ResponseEntity;

import static it.EndpointsKt.CUSTOM_STATUS_CODE_GET_ENDPOINT;
import static it.EndpointsKt.MULTIPLE_PATH_PARAM_GET_ENDPOINT;
import static it.EndpointsKt.QUERY_PARAM_GET_ENDPOINT;
import static it.EndpointsKt.SIMPLE_GET_ENDPOINT;
import static it.EndpointsKt.SINGLE_PATH_PARAM_GET_ENDPOINT;
import static it.UtilsKt.PATH_PARAM_1;
import static it.UtilsKt.PATH_PARAM_2;
import static it.UtilsKt.QUERY_PARAM_1;
import static it.UtilsKt.QUERY_PARAM_2;
import static java.text.MessageFormat.format;

public class GetController {

    @Endpoint(path = SIMPLE_GET_ENDPOINT, method = HttpMethod.GET)
    public HttpResponse<ResponseEntity> simpleGetNoParams() {
        return HttpResponse.ok(new ResponseEntity(SIMPLE_GET_ENDPOINT));
    }

    @Endpoint(path = QUERY_PARAM_GET_ENDPOINT, method = HttpMethod.GET)
    public HttpResponse<ResponseEntity> signleQueryParamTest(@QueryParam(value = QUERY_PARAM_1) String queryParam) {
        return HttpResponse.ok(new ResponseEntity(format("{0}-{1}", QUERY_PARAM_GET_ENDPOINT, queryParam)));
    }

    @Endpoint(path = QUERY_PARAM_GET_ENDPOINT, method = HttpMethod.GET)
    public HttpResponse<ResponseEntity> multipleQueryParamTest(
            @QueryParam(QUERY_PARAM_1) String queryParam1,
            @QueryParam(QUERY_PARAM_2) String queryParam2) {

        return HttpResponse.ok(
                new ResponseEntity(
                        format(
                                "{0}-{1},{2}",
                                QUERY_PARAM_GET_ENDPOINT,
                                queryParam1,
                                queryParam2)));
    }

    @Endpoint(path = SINGLE_PATH_PARAM_GET_ENDPOINT, method = HttpMethod.GET)
    public HttpResponse<ResponseEntity> singlePathParamPath(
            @PathParam(PATH_PARAM_1) String pathParam) {

        return HttpResponse.ok(
                new ResponseEntity(
                        format(
                                "{0}-{1}",
                                SINGLE_PATH_PARAM_GET_ENDPOINT,
                                pathParam)));
    }

    @Endpoint(path = MULTIPLE_PATH_PARAM_GET_ENDPOINT, method = HttpMethod.GET)
    public HttpResponse<ResponseEntity> multiplePathParamPath(
            @PathParam(PATH_PARAM_1) String pathParamOne,
            @PathParam(PATH_PARAM_2) String pathParamTwo) {

        return HttpResponse.ok(
                new ResponseEntity(
                        format(
                                "{0}-{1},{2}",
                                MULTIPLE_PATH_PARAM_GET_ENDPOINT,
                                pathParamOne,
                                pathParamTwo)));
    }

    @Endpoint(path = MULTIPLE_PATH_PARAM_GET_ENDPOINT, method = HttpMethod.GET)
    public HttpResponse<ResponseEntity> multiplePathParamWithQueryParamsPath(
            @QueryParam(QUERY_PARAM_1) String queryParam,
            @PathParam(PATH_PARAM_1) String pathParamOne,
            @PathParam(PATH_PARAM_2) String pathParamTwo) {

        return HttpResponse.ok(
                new ResponseEntity(
                        format(
                                "{0}-{1},{2},{3}",
                                MULTIPLE_PATH_PARAM_GET_ENDPOINT,
                                queryParam,
                                pathParamOne,
                                pathParamTwo)));
    }

    @Endpoint(path = CUSTOM_STATUS_CODE_GET_ENDPOINT, method = HttpMethod.GET)
    public HttpResponse<Object> customStatusCodeEndpoint() {
        return HttpResponse.withCode(StatusCode.ACCEPTED);
    }

}
