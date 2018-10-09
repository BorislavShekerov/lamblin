package com.lamblin.it.controller;

import com.google.common.collect.ImmutableSet;

import com.lamblin.it.controller.client.PutControllerClient;
import com.lamblin.it.model.ExampleRequestBody;
import com.lamblin.test.config.LamblinTestConfig;
import com.lamblin.test.config.annotation.LamblinTestRunnerConfig;
import com.lamblin.test.junit4.JUnit4LamblinTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static com.lamblin.it.model.EndpointsKt.MULTI_PATH_PARAM_PUT_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.QUERY_PARAM_PUT_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SIMPLE_PUT_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SIMPLE_REQUEST_BODY_PUT_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SINGLE_PATH_PARAM_PUT_ENDPOINT;
import static com.lamblin.it.model.TestUtilsKt.runRequestAndVerifyResponse;
import static java.text.MessageFormat.format;

@RunWith(JUnit4LamblinTestRunner.class)
@LamblinTestRunnerConfig(testConfigClass = PutControllerTest.TestConfiguration.class)
public class PutControllerTest {


    private static final PutControllerClient client = PutControllerClient.INSTANCE;

    @Test
    public void shouldHandlePutRequestsWithNoParams() {
        runRequestAndVerifyResponse(
                client::callSimplePostNoParamsEndpoint,
                SIMPLE_PUT_ENDPOINT);
    }

    @Test
    public void shouldHandlePutRequestsWithSingleQueryParam() {
        String queryParamValue = "value";

        runRequestAndVerifyResponse(
                () -> client.callSingleQueryParamEndpoint(queryParamValue),
                format(
                        "{0}-{1}",
                        QUERY_PARAM_PUT_ENDPOINT,
                        queryParamValue));
    }

    @Test
    public void shouldHandlePutRequestsWithMultipleQueryParams() {
        String queryParam1Value = "value1";
        String queryParam2Value = "value2";

        runRequestAndVerifyResponse(
                () -> client.callMultiQueryParamEndpoint(queryParam1Value, queryParam2Value),
                format(
                        "{0}-{1},{2}",
                        QUERY_PARAM_PUT_ENDPOINT,
                        queryParam1Value,
                        queryParam2Value));
    }

    @Test
    public void shouldHandlePutRequestsWithSinglePathParam() {
        String pathParamValue = "value";

        runRequestAndVerifyResponse(
                () -> client.callSinglePathParamEndpoint(pathParamValue),
                format(
                        "{0}-{1}",
                        SINGLE_PATH_PARAM_PUT_ENDPOINT,
                        pathParamValue));
    }

    @Test
    public void shouldHandlePutRequestsWithMultiplePathParams() {
        String pathParamValue1 = "value1";
        String pathParamValue2 = "value2";

        runRequestAndVerifyResponse(
                () -> client.callMultiPathParamEndpoint(pathParamValue1, pathParamValue2),
                format(
                        "{0}-{1},{2}",
                        MULTI_PATH_PARAM_PUT_ENDPOINT,
                        pathParamValue1,
                        pathParamValue2));
    }

    @Test
    public void shouldHandlePostRequestsWithMultiplePathParamsAndQueryParams() {
        String queryParamValue = "queryParamValue";
        String pathParamValue1 = "value1";
        String pathParamValue2 = "value2";

        runRequestAndVerifyResponse(
                () -> client.callMultiPathParamEndpointWithQueryParamEndpoint(queryParamValue, pathParamValue1, pathParamValue2),
                format(
                        "{0}-{1},{2},{3}",
                        MULTI_PATH_PARAM_PUT_ENDPOINT,
                        queryParamValue,
                        pathParamValue1,
                        pathParamValue2));
    }


    @Test
    public void shouldHandlePostRequestsWithRequestBody() {
        String bodyContent = "test";
        ExampleRequestBody requestBody = new ExampleRequestBody(bodyContent);

        runRequestAndVerifyResponse(
                () -> client.callRequestBodyEndpoint(requestBody),
                format("{0}-{1}", SIMPLE_REQUEST_BODY_PUT_ENDPOINT, bodyContent));
    }

    public static class TestConfiguration implements LamblinTestConfig {

        @Override
        public Set<Object> controllers() {
            return ImmutableSet.of(new PutController());
        }
    }

}
