package com.lamblin.it.controller;

import static com.lamblin.it.model.EndpointsKt.MULTIPLE_PATH_PARAM_PATH_PATCH_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.QUERY_PARAM_PATCH_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SIMPLE_PATCH_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SIMPLE_REQUEST_BODY_PATCH_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SINGLE_PATH_PARAM_PATH_PATCH_ENDPOINT;
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
import com.lamblin.it.model.ExampleRequestBody;

public class PatchControllerIntegrationTest {

    private FrontController frontController;

    {
        Set<Object> controllers = new HashSet<>();
        controllers.add(new PatchController());
        frontController = FrontController.instance(controllers);
    }

    @Test
    public void shouldHandlePatchRequestsWithNoParams() {
        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(SIMPLE_PATCH_ENDPOINT, HttpMethod.PATCH),
                SIMPLE_PATCH_ENDPOINT);
    }

    @Test
    public void shouldHandlePatchRequestsWithSingleQueryParam() {
        String queryParamValue = "value";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_PATCH_ENDPOINT,
                        HttpMethod.PATCH,
                        ImmutableMap.of(QUERY_PARAM_1, queryParamValue)),
                format(
                        "{0}-{1}",
                        QUERY_PARAM_PATCH_ENDPOINT,
                        queryParamValue));
    }

    @Test
    public void shouldHandlePatchRequestsWithMultipleQueryParams() {
        String queryParam1Value = "value1";
        String queryParam2Value = "value2";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        QUERY_PARAM_PATCH_ENDPOINT,
                        HttpMethod.PATCH,
                        ImmutableMap.of(
                                QUERY_PARAM_1, queryParam1Value,
                                QUERY_PARAM_2, queryParam2Value)),
                format(
                        "{0}-{1},{2}",
                        QUERY_PARAM_PATCH_ENDPOINT,
                        queryParam1Value,
                        queryParam2Value));
    }

    @Test
    public void shouldHandlePatchRequestsWithSinglePathParam() {
        String pathParamValue = "value";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        SINGLE_PATH_PARAM_PATH_PATCH_ENDPOINT.replace("{" + PATH_PARAM_1 + "}", pathParamValue),
                        HttpMethod.PATCH),
                format(
                        "{0}-{1}",
                        SINGLE_PATH_PARAM_PATH_PATCH_ENDPOINT,
                        pathParamValue));
    }

    @Test
    public void shouldHandlePatchRequestsWithMultiplePathParams() {
        String pathParamValue1 = "value1";
        String pathParamValue2 = "value2";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_PATH_PATCH_ENDPOINT
                                .replace("{" + PATH_PARAM_1 + "}", pathParamValue1)
                                .replace("{" + PATH_PARAM_2 + "}", pathParamValue2),
                        HttpMethod.PATCH),
                format(
                        "{0}-{1},{2}",
                        MULTIPLE_PATH_PARAM_PATH_PATCH_ENDPOINT,
                        pathParamValue1,
                        pathParamValue2));
    }

    @Test
    public void shouldHandlePatchRequestsWithMultiplePathParamsAndQueryParams() {
        String queryParamValue = "queryParamValue";
        String pathParamValue1 = "value1";
        String pathParamValue2 = "value2";

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        MULTIPLE_PATH_PARAM_PATH_PATCH_ENDPOINT
                                .replace("{" + PATH_PARAM_1 + "}", pathParamValue1)
                                .replace("{" + PATH_PARAM_2 + "}", pathParamValue2),
                        HttpMethod.PATCH,
                        ImmutableMap.of(QUERY_PARAM_1, queryParamValue)),
                format(
                        "{0}-{1},{2},{3}",
                        MULTIPLE_PATH_PARAM_PATH_PATCH_ENDPOINT,
                        queryParamValue,
                        pathParamValue1,
                        pathParamValue2));
    }


    @Test
    public void shouldHandlePatchRequestsWithRequestBody() {
        String bodyContent = "test";
        ExampleRequestBody requestBody = new ExampleRequestBody(bodyContent);

        runRequestAndVerifyResponse(
                frontController,
                createRequestInputStream(
                        SIMPLE_REQUEST_BODY_PATCH_ENDPOINT,
                        HttpMethod.PATCH,
                        requestBody),
                format("{0}-{1}", SIMPLE_REQUEST_BODY_PATCH_ENDPOINT, bodyContent));
    }
}
