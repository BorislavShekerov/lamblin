package it.java.controller;

import com.lamblin.core.model.HttpMethod;
import com.lamblin.core.model.HttpResponse;
import com.lamblin.core.model.annotation.Endpoint;
import com.lamblin.core.model.annotation.PathParam;
import com.lamblin.core.model.annotation.QueryParam;
import com.lamblin.core.model.annotation.RequestBody;

import it.ExampleRequestBody;
import it.ResponseEntity;

import static it.EndpointsKt.MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT;
import static it.EndpointsKt.QUERY_PARAM_PARAM_PUT_ENDPOINT;
import static it.EndpointsKt.SIMPLE_PUT_ENDPOINT;
import static it.EndpointsKt.SIMPLE_REQUEST_BODY_PUT_ENDPOINT;
import static it.EndpointsKt.SINGLE_PATH_PARAM_PATH_PUT_ENDPOINT;
import static it.UtilsKt.PATH_PARAM_1;
import static it.UtilsKt.PATH_PARAM_2;
import static it.UtilsKt.QUERY_PARAM_1;
import static it.UtilsKt.QUERY_PARAM_2;
import static java.text.MessageFormat.format;

public class PutController {

    @Endpoint(path = SIMPLE_PUT_ENDPOINT, method = HttpMethod.PUT)
    public HttpResponse<ResponseEntity> simplePutNoParams() {
        return HttpResponse.ok(new ResponseEntity(SIMPLE_PUT_ENDPOINT));
    }

    @Endpoint(path = QUERY_PARAM_PARAM_PUT_ENDPOINT, method = HttpMethod.PUT)
    public HttpResponse<ResponseEntity> signleQueryParamTest(@QueryParam(value = QUERY_PARAM_1) String queryParam) {
        return HttpResponse.ok(
                new ResponseEntity(format("{0}-{1}", QUERY_PARAM_PARAM_PUT_ENDPOINT, queryParam)));
    }

    @Endpoint(path = QUERY_PARAM_PARAM_PUT_ENDPOINT, method = HttpMethod.PUT)
    public HttpResponse<ResponseEntity> multipleQueryParamTest(
            @QueryParam(QUERY_PARAM_1) String queryParam1,
            @QueryParam(QUERY_PARAM_2) String queryParam2) {

        return HttpResponse.ok(
                new ResponseEntity(
                        format(
                                "{0}-{1},{2}",
                                QUERY_PARAM_PARAM_PUT_ENDPOINT,
                                queryParam1,
                                queryParam2)));
    }

    @Endpoint(path = SINGLE_PATH_PARAM_PATH_PUT_ENDPOINT, method = HttpMethod.PUT)
    public HttpResponse<ResponseEntity> singlePathParamPath(
            @PathParam(PATH_PARAM_1) String pathParam) {

        return HttpResponse.ok(
                new ResponseEntity(
                        format(
                                "{0}-{1}",
                                SINGLE_PATH_PARAM_PATH_PUT_ENDPOINT,
                                pathParam)));
    }

    @Endpoint(path = MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT, method = HttpMethod.PUT)
    public HttpResponse<ResponseEntity> multiplePathParamPath(
            @PathParam(PATH_PARAM_1) String pathParamOne,
            @PathParam(PATH_PARAM_2) String pathParamTwo) {

        return HttpResponse.ok(
                new ResponseEntity(
                        format(
                                "{0}-{1},{2}",
                                MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT,
                                pathParamOne,
                                pathParamTwo)));
    }

    @Endpoint(path = MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT, method = HttpMethod.PUT)
    public HttpResponse<ResponseEntity> multiplePathParamWithQueryParamsPath(
            @QueryParam(QUERY_PARAM_1) String queryParam,
            @PathParam(PATH_PARAM_1) String pathParamOne,
            @PathParam(PATH_PARAM_2) String pathParamTwo) {

        return HttpResponse.ok(
                new ResponseEntity(
                        format(
                                "{0}-{1},{2},{3}",
                                MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT,
                                queryParam,
                                pathParamOne,
                                pathParamTwo)));
    }

    @Endpoint(path = SIMPLE_REQUEST_BODY_PUT_ENDPOINT, method = HttpMethod.PUT)
    public HttpResponse<ResponseEntity> requestBody(@RequestBody ExampleRequestBody exampleRequestBody) {

        return HttpResponse.ok(
                new ResponseEntity(
                        format("{0}-{1}", SIMPLE_REQUEST_BODY_PUT_ENDPOINT, exampleRequestBody.getBody())));
    }

}
