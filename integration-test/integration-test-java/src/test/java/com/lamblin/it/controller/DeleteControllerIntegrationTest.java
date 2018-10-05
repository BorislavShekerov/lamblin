package com.lamblin.it.controller;

import static com.lamblin.it.model.EndpointsKt.MULTIPLE_PATH_PARAM_DELETE_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.QUERY_PARAM_DELETE_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SIMPLE_DELETE_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SINGLE_PATH_PARAM_DELETE_ENDPOINT;
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

public class DeleteControllerIntegrationTest {

    private FrontController frontController;

    {
        Set<Object> controllers = new HashSet<>();
        controllers.add(new DeleteController());
        frontController = FrontController.instance(controllers);
    }

    @Test
    public void shouldHandleDeleteRequestsWithNoParams() {
        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(SIMPLE_DELETE_ENDPOINT, HttpMethod.DELETE),
                SIMPLE_DELETE_ENDPOINT);
    }

    @Test
    public void shouldHandleDeleteRequestsWithSingleQueryParam() {
        String queryParamValue = "value";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_DELETE_ENDPOINT,
                        HttpMethod.DELETE,
                        ImmutableMap.of(QUERY_PARAM_1, queryParamValue)),
                format(
                        "{0}-{1}",
                        QUERY_PARAM_DELETE_ENDPOINT,
                        queryParamValue));
    }

    @Test
    public void shouldHandleDeleteRequestsWithMultipleQueryParams() {
        String queryParam1Value = "value1";
        String queryParam2Value = "value2";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_DELETE_ENDPOINT,
                        HttpMethod.DELETE,
                        ImmutableMap.of(
                                QUERY_PARAM_1, queryParam1Value,
                                QUERY_PARAM_2, queryParam2Value)),
                format(
                        "{0}-{1},{2}",
                        QUERY_PARAM_DELETE_ENDPOINT,
                        queryParam1Value,
                        queryParam2Value));
    }

    @Test
    public void shouldHandleDeleteRequestsWithSinglePathParam() {
        String pathParamValue = "value";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        SINGLE_PATH_PARAM_DELETE_ENDPOINT.replace("{" + PATH_PARAM_1 + "}", pathParamValue),
                        HttpMethod.DELETE),
                format(
                        "{0}-{1}",
                        SINGLE_PATH_PARAM_DELETE_ENDPOINT,
                        pathParamValue));
    }

    @Test
    public void shouldHandleDeleteRequestsWithMultiplePathParams() {
        String pathParamValue1 = "value1";
        String pathParamValue2 = "value2";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_DELETE_ENDPOINT
                                .replace("{" + PATH_PARAM_1 + "}", pathParamValue1)
                                .replace("{" + PATH_PARAM_2 + "}", pathParamValue2),
                        HttpMethod.DELETE),
                format(
                        "{0}-{1},{2}",
                        MULTIPLE_PATH_PARAM_DELETE_ENDPOINT,
                        pathParamValue1,
                        pathParamValue2));
    }

    @Test
    public void shouldHandleDeleteRequestsWithMultiplePathParamsAndQueryParams() {
        String queryParamValue = "queryParamValue";
        String pathParamValue1 = "value1";
        String pathParamValue2 = "value2";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_DELETE_ENDPOINT
                                .replace("{" + PATH_PARAM_1 + "}", pathParamValue1)
                                .replace("{" + PATH_PARAM_2 + "}", pathParamValue2),
                        HttpMethod.DELETE,
                        ImmutableMap.of(QUERY_PARAM_1, queryParamValue)),
                format(
                        "{0}-{1},{2},{3}",
                        MULTIPLE_PATH_PARAM_DELETE_ENDPOINT,
                        queryParamValue,
                        pathParamValue1,
                        pathParamValue2));
    }

}
