package com.lamblin.it.controller;

import com.google.common.collect.ImmutableMap;

import com.lamblin.core.FrontController;
import com.lamblin.core.model.HttpMethod;
import com.lamblin.it.model.ExampleRequestBody;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.lamblin.it.model.EndpointsKt.MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.QUERY_PARAM_PARAM_PUT_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SIMPLE_PUT_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SIMPLE_REQUEST_BODY_PUT_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SINGLE_PATH_PARAM_PATH_PUT_ENDPOINT;
import static com.lamblin.it.model.TestUtilsKt.PATH_PARAM_1;
import static com.lamblin.it.model.TestUtilsKt.PATH_PARAM_2;
import static com.lamblin.it.model.TestUtilsKt.QUERY_PARAM_1;
import static com.lamblin.it.model.TestUtilsKt.QUERY_PARAM_2;
import static com.lamblin.it.model.TestUtilsKt.createRequestInputStream;
import static com.lamblin.it.model.TestUtilsKt.runRequestAndVerifyResponse;
import static java.text.MessageFormat.format;

public class PutControllerTest {


    private FrontController frontController;

    {
        Set<Object> controllers = new HashSet<>();
        controllers.add(new PutController());
        frontController = FrontController.instance(controllers);
    }

    @Test
    public void shouldHandlePutRequestsWithNoParams() {
        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(SIMPLE_PUT_ENDPOINT, HttpMethod.PUT),
                SIMPLE_PUT_ENDPOINT);
    }

    @Test
    public void shouldHandlePutRequestsWithSingleQueryParam() {
        String queryParamValue = "value";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_PARAM_PUT_ENDPOINT,
                        HttpMethod.PUT,
                        ImmutableMap.of(QUERY_PARAM_1, queryParamValue)),
                format(
                        "{0}-{1}",
                        QUERY_PARAM_PARAM_PUT_ENDPOINT,
                        queryParamValue));
    }

    @Test
    public void shouldHandlePutRequestsWithMultipleQueryParams() {
        String queryParam1Value = "value1";
        String queryParam2Value = "value2";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_PARAM_PUT_ENDPOINT,
                        HttpMethod.PUT,
                        ImmutableMap.of(
                                QUERY_PARAM_1, queryParam1Value,
                                QUERY_PARAM_2, queryParam2Value)),
                format(
                        "{0}-{1},{2}",
                        QUERY_PARAM_PARAM_PUT_ENDPOINT,
                        queryParam1Value,
                        queryParam2Value));
    }

    @Test
    public void shouldHandlePutRequestsWithSinglePathParam() {
        String pathParamValue = "value";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        SINGLE_PATH_PARAM_PATH_PUT_ENDPOINT.replace("{" + PATH_PARAM_1 + "}", pathParamValue),
                        HttpMethod.PUT),
                format(
                        "{0}-{1}",
                        SINGLE_PATH_PARAM_PATH_PUT_ENDPOINT,
                        pathParamValue));
    }

    @Test
    public void shouldHandlePutRequestsWithMultiplePathParams() {
        String pathParamValue1 = "value1";
        String pathParamValue2 = "value2";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT
                                .replace("{" + PATH_PARAM_1 + "}", pathParamValue1)
                                .replace("{" + PATH_PARAM_2 + "}", pathParamValue2),
                        HttpMethod.PUT),
                format(
                        "{0}-{1},{2}",
                        MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT,
                        pathParamValue1,
                        pathParamValue2));
    }

    @Test
    public void shouldHandlePostRequestsWithMultiplePathParamsAndQueryParams() {
        String queryParamValue = "queryParamValue";
        String pathParamValue1 = "value1";
        String pathParamValue2 = "value2";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT
                                .replace("{" + PATH_PARAM_1 + "}", pathParamValue1)
                                .replace("{" + PATH_PARAM_2 + "}", pathParamValue2),
                        HttpMethod.PUT,
                        ImmutableMap.of(QUERY_PARAM_1, queryParamValue)),
                format(
                        "{0}-{1},{2},{3}",
                        MULTIPLE_PATH_PARAM_PATH_PUT_ENDPOINT,
                        queryParamValue,
                        pathParamValue1,
                        pathParamValue2));
    }


    @Test
    public void shouldHandlePostRequestsWithRequestBody() {
        String bodyContent = "test";
        ExampleRequestBody requestBody = new ExampleRequestBody(bodyContent);

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        SIMPLE_REQUEST_BODY_PUT_ENDPOINT,
                        HttpMethod.PUT,
                        requestBody),
                format("{0}-{1}", SIMPLE_REQUEST_BODY_PUT_ENDPOINT, bodyContent));
    }

}
