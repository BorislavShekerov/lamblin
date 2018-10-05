package com.lamblin.it.controller;

import static com.lamblin.it.model.EndpointsKt.CUSTOM_STATUS_CODE_GET_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.MULTIPLE_PATH_PARAM_GET_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.QUERY_PARAM_GET_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SIMPLE_GET_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SINGLE_PATH_PARAM_GET_ENDPOINT;
import static com.lamblin.it.model.TestUtilsKt.PATH_PARAM_1;
import static com.lamblin.it.model.TestUtilsKt.PATH_PARAM_2;
import static com.lamblin.it.model.TestUtilsKt.QUERY_PARAM_1;
import static com.lamblin.it.model.TestUtilsKt.QUERY_PARAM_2;
import static com.lamblin.it.model.TestUtilsKt.createRequestInputStream;
import static com.lamblin.it.model.TestUtilsKt.runRequestAndVerifyResponse;
import static java.text.MessageFormat.format;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.lamblin.core.FrontController;
import com.lamblin.core.model.HttpMethod;
import com.lamblin.core.model.StatusCode;

public class GetControllerIntegrationTest {

    private FrontController frontController;

    {
        Set<Object> controllers = new HashSet<>();
        controllers.add(new GetController());
        frontController = FrontController.instance(controllers);
    }

    @Test
    public void shouldHandleGetRequestsWithNoParams() {
        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(SIMPLE_GET_ENDPOINT, HttpMethod.GET),
                SIMPLE_GET_ENDPOINT);
    }

    @Test
    public void shouldHandleGetRequestsWithSingleQueryParam() {
        String queryParamValue = "value";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_GET_ENDPOINT,
                        HttpMethod.GET,
                        ImmutableMap.of(QUERY_PARAM_1, queryParamValue)),
                format(
                        "{0}-{1}",
                        QUERY_PARAM_GET_ENDPOINT,
                        queryParamValue));
    }

    @Test
    public void shouldHandleGetRequestsWithMultipleQueryParams() {
        String queryParam1Value = "value1";
        String queryParam2Value = "value2";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_GET_ENDPOINT,
                        HttpMethod.GET,
                        ImmutableMap.of(
                                QUERY_PARAM_1, queryParam1Value,
                                QUERY_PARAM_2, queryParam2Value)),
                format(
                        "{0}-{1},{2}",
                        QUERY_PARAM_GET_ENDPOINT,
                        queryParam1Value,
                        queryParam2Value));
    }

    @Test
    public void shouldHandleGetRequestsWithSinglePathParam() {
        String pathParamValue = "value";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        SINGLE_PATH_PARAM_GET_ENDPOINT.replace("{" + PATH_PARAM_1 + "}", pathParamValue),
                        HttpMethod.GET),
                format(
                        "{0}-{1}",
                        SINGLE_PATH_PARAM_GET_ENDPOINT,
                        pathParamValue));
    }

    @Test
    public void shouldHandleGetRequestsWithMultiplePathParams() {
        String pathParamValue1 = "value1";
        String pathParamValue2 = "value2";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_GET_ENDPOINT
                                .replace("{" + PATH_PARAM_1 + "}", pathParamValue1)
                                .replace("{" + PATH_PARAM_2 + "}", pathParamValue2),
                        HttpMethod.GET),
                format(
                        "{0}-{1},{2}",
                        MULTIPLE_PATH_PARAM_GET_ENDPOINT,
                        pathParamValue1,
                        pathParamValue2));
    }

    @Test
    public void shouldHandleGetRequestsWithMultiplePathParamsAndQueryParams() {
        String queryParamValue = "queryParamValue";
        String pathParamValue1 = "value1";
        String pathParamValue2 = "value2";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_GET_ENDPOINT
                                .replace("{" + PATH_PARAM_1 + "}", pathParamValue1)
                                .replace("{" + PATH_PARAM_2 + "}", pathParamValue2),
                        HttpMethod.GET,
                        ImmutableMap.of(QUERY_PARAM_1, queryParamValue)),
                format(
                        "{0}-{1},{2},{3}",
                        MULTIPLE_PATH_PARAM_GET_ENDPOINT,
                        queryParamValue,
                        pathParamValue1,
                        pathParamValue2));
    }

    @Test
    public void shouldReturn404ForUnknownRoutes() {
        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream("/unknown", HttpMethod.GET),
                 404);
    }

    @Test
    public void shouldReturnStatusCodeReturnedFromEndpoint() {
        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(CUSTOM_STATUS_CODE_GET_ENDPOINT, HttpMethod.GET),
                StatusCode.ACCEPTED.getCode());
    }

}
