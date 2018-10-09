package com.lamblin.it.controller;

import com.lamblin.core.model.HttpMethod;
import com.lamblin.core.model.HttpResponse;
import com.lamblin.core.model.annotation.*;
import com.lamblin.it.model.ExampleRequestBody;
import com.lamblin.it.model.ResponseEntity;

import static com.lamblin.it.model.EndpointsKt.*;
import static com.lamblin.it.model.TestUtilsKt.*;
import static java.text.MessageFormat.format;

@Controller
public class PostController {

    @Endpoint(path = SIMPLE_POST_ENDPOINT, method = HttpMethod.POST)
    public HttpResponse<ResponseEntity> simpleGetNoParams() {
        return HttpResponse.ok(new ResponseEntity(SIMPLE_POST_ENDPOINT));
    }

    @Endpoint(path = QUERY_PARAM_POST_ENDPOINT, method = HttpMethod.POST)
    public HttpResponse<ResponseEntity> signleQueryParamTest(@QueryParam(value = QUERY_PARAM_1) String queryParam) {
        return HttpResponse.ok(
                new ResponseEntity(format("{0}-{1}", QUERY_PARAM_POST_ENDPOINT, queryParam)));
    }

    @Endpoint(path = QUERY_PARAM_POST_ENDPOINT, method = HttpMethod.POST)
    public HttpResponse<ResponseEntity> multipleQueryParamTest(
            @QueryParam(QUERY_PARAM_1) String queryParam1,
            @QueryParam(QUERY_PARAM_2) String queryParam2) {

        return HttpResponse.ok(
                new ResponseEntity(
                        format(
                                "{0}-{1},{2}",
                                QUERY_PARAM_POST_ENDPOINT,
                                queryParam1,
                                queryParam2)));
    }

    @Endpoint(path = SINGLE_PATH_PARAM_POST_ENDPOINT, method = HttpMethod.POST)
    public HttpResponse<ResponseEntity> singlePathParamPath(
            @PathParam(PATH_PARAM_1) String pathParam) {

        return HttpResponse.ok(
                new ResponseEntity(
                        format(
                                "{0}-{1}",
                                SINGLE_PATH_PARAM_POST_ENDPOINT,
                                pathParam)));
    }

    @Endpoint(path = MULTI_PATH_PARAM_POST_ENDPOINT, method = HttpMethod.POST)
    public HttpResponse<ResponseEntity> multiplePathParamPath(
            @PathParam(PATH_PARAM_1) String pathParamOne,
            @PathParam(PATH_PARAM_2) String pathParamTwo) {

        return HttpResponse.ok(
                new ResponseEntity(
                        format(
                                "{0}-{1},{2}",
                                MULTI_PATH_PARAM_POST_ENDPOINT,
                                pathParamOne,
                                pathParamTwo)));
    }

    @Endpoint(path = MULTI_PATH_PARAM_POST_ENDPOINT, method = HttpMethod.POST)
    public HttpResponse<ResponseEntity> multiplePathParamWithQueryParamsPath(
            @QueryParam(QUERY_PARAM_1) String queryParam,
            @PathParam(PATH_PARAM_1) String pathParamOne,
            @PathParam(PATH_PARAM_2) String pathParamTwo) {

        return HttpResponse.ok(
                new ResponseEntity(
                        format(
                                "{0}-{1},{2},{3}",
                                MULTI_PATH_PARAM_POST_ENDPOINT,
                                queryParam,
                                pathParamOne,
                                pathParamTwo)));
    }

    @Endpoint(path = SIMPLE_REQUEST_BODY_POST_ENDPOINT, method = HttpMethod.POST)
    public HttpResponse<ResponseEntity> requestBody(@RequestBody ExampleRequestBody exampleRequestBody) {

        return HttpResponse.ok(
                new ResponseEntity(
                        format("{0}-{1}", SIMPLE_REQUEST_BODY_POST_ENDPOINT, exampleRequestBody.getBody())));
    }

}
