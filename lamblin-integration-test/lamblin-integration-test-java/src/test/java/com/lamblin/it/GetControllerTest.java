package com.lamblin.it;

import com.google.common.collect.ImmutableSet;

import com.lamblin.core.model.StatusCode;
import com.lamblin.it.controller.GetController;
import com.lamblin.it.client.GetControllerClient;
import com.lamblin.test.config.LamblinTestConfig;
import com.lamblin.test.config.annotation.LamblinTestRunnerConfig;
import com.lamblin.test.junit4.JUnit4LamblinTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static com.lamblin.it.model.EndpointsKt.*;
import static com.lamblin.it.model.TestUtilsKt.DEFAULT_QUERY_PARAM_VALUE;
import static com.lamblin.it.model.TestUtilsKt.runRequestAndVerifyResponse;
import static java.text.MessageFormat.format;

@RunWith(JUnit4LamblinTestRunner.class)
@LamblinTestRunnerConfig(testConfigClass = GetControllerTest.TestConfiguration.class)
public class GetControllerTest {

    private static final GetControllerClient client = GetControllerClient.INSTANCE;

    @Test
    public void shouldHandleGetRequestsWithNoParams() {
        runRequestAndVerifyResponse(
                client::callSimpleGetNoParamsEndpoint,
                SIMPLE_GET_ENDPOINT);
    }

    @Test
    public void shouldHandleGetRequestsWithSingleQueryParam() {
        String queryParamValue = "value";

        runRequestAndVerifyResponse(
                () -> client.callSingleQueryParamEndpoint(queryParamValue),
                format(
                        "{0}-{1}",
                        QUERY_PARAM_GET_ENDPOINT,
                        queryParamValue));
    }

    @Test
    public void shouldHandleGetRequestsWithQueryParamWithDefaultValue() {
        runRequestAndVerifyResponse(
                client::callQueryParamDefaultValueEndpoint,
                format(
                        "{0}-{1}",
                        QUERY_PARAM_DEFAULT_VALUE_GET_ENDPOINT,
                        DEFAULT_QUERY_PARAM_VALUE));
    }

    @Test
    public void shouldHandleGetRequestsWithMultipleQueryParams() {
        String queryParam1Value = "value1";
        String queryParam2Value = "value2";

        runRequestAndVerifyResponse(
                () -> client.callMultiQueryParamEndpoint(queryParam1Value, queryParam2Value),
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
                () -> client.callSinglePathParamEndpoint(pathParamValue),
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
                () -> client.callMultiPathParamEndpoint(pathParamValue1, pathParamValue2),
                format(
                        "{0}-{1},{2}",
                        MULTI_PATH_PARAM_GET_ENDPOINT,
                        pathParamValue1,
                        pathParamValue2));
    }

    @Test
    public void shouldHandleGetRequestsWithMultiplePathParamsAndQueryParams() {
        String queryParamValue = "queryParamValue";
        String pathParamValue1 = "value1";
        String pathParamValue2 = "value2";

        runRequestAndVerifyResponse(
                () -> client.callMultiPathParamWithQueryParamEndpoint(queryParamValue, pathParamValue1, pathParamValue2),
                format(
                        "{0}-{1},{2},{3}",
                        MULTI_PATH_PARAM_GET_ENDPOINT,
                        queryParamValue,
                        pathParamValue1,
                        pathParamValue2));
    }

    public static class TestConfiguration implements LamblinTestConfig {

        @Override
        public Set<Object> controllers() {
            return ImmutableSet.of(new GetController());
        }
    }

}
