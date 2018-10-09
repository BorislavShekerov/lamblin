package com.lamblin.it.controller;

import com.google.common.collect.ImmutableSet;

import com.lamblin.it.controller.client.PostControllerClient;
import com.lamblin.it.model.ExampleRequestBody;
import com.lamblin.test.config.LamblinTestConfig;
import com.lamblin.test.config.annotation.LamblinTestRunnerConfig;
import com.lamblin.test.junit4.JUnit4LamblinTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static com.lamblin.it.model.EndpointsKt.MULTI_PATH_PARAM_POST_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.QUERY_PARAM_POST_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SIMPLE_POST_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SIMPLE_REQUEST_BODY_POST_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SINGLE_PATH_PARAM_POST_ENDPOINT;
import static com.lamblin.it.model.TestUtilsKt.runRequestAndVerifyResponse;
import static java.text.MessageFormat.format;

@RunWith(JUnit4LamblinTestRunner.class)
@LamblinTestRunnerConfig(testConfigClass = PostControllerTest.TestConfiguration.class)
public class PostControllerTest {

    private static final PostControllerClient client = PostControllerClient.INSTANCE;

    @Test
    public void shouldHandlePostRequestsWithNoParams() {
        runRequestAndVerifyResponse(
                client::callSimplePostNoParamsEndpoint,
                SIMPLE_POST_ENDPOINT);
    }

    @Test
    public void shouldHandlePostRequestsWithSingleQueryParam() {
        String queryParamValue = "value";

        runRequestAndVerifyResponse(
                () -> client.callSingleQueryParamEndpoint(queryParamValue),
                format(
                        "{0}-{1}",
                        QUERY_PARAM_POST_ENDPOINT,
                        queryParamValue));
    }

    @Test
    public void shouldHandlePostRequestsWithMultipleQueryParams() {
        String queryParam1Value = "value1";
        String queryParam2Value = "value2";

        runRequestAndVerifyResponse(
                () -> client.callMultiQueryParamEndpoint(queryParam1Value, queryParam2Value),
                format(
                        "{0}-{1},{2}",
                        QUERY_PARAM_POST_ENDPOINT,
                        queryParam1Value,
                        queryParam2Value));
    }

    @Test
    public void shouldHandlePostRequestsWithSinglePathParam() {
        String pathParamValue = "value";

        runRequestAndVerifyResponse(
                () -> client.callSinglePathParamEndpoint(pathParamValue),
                format(
                        "{0}-{1}",
                        SINGLE_PATH_PARAM_POST_ENDPOINT,
                        pathParamValue));
    }

    @Test
    public void shouldHandlePostRequestsWithMultiplePathParams() {
        String pathParamValue1 = "value1";
        String pathParamValue2 = "value2";

        runRequestAndVerifyResponse(
                () -> client.callMultiPathParamEndpoint(pathParamValue1, pathParamValue2),
                format(
                        "{0}-{1},{2}",
                        MULTI_PATH_PARAM_POST_ENDPOINT,
                        pathParamValue1,
                        pathParamValue2));
    }

    @Test
    public void shouldHandlePostRequestsWithMultiplePathParamsAndQueryParams() {
        String queryParamValue = "queryParamValue";
        String pathParamValue1 = "value1";
        String pathParamValue2 = "value2";

        runRequestAndVerifyResponse(
                () -> client.callMultiPathParamEndpointWithQueryParam(queryParamValue, pathParamValue1, pathParamValue2),
                format(
                        "{0}-{1},{2},{3}",
                        MULTI_PATH_PARAM_POST_ENDPOINT,
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
                format("{0}-{1}", SIMPLE_REQUEST_BODY_POST_ENDPOINT, bodyContent));
    }

    public static class TestConfiguration implements LamblinTestConfig {

        @Override
        public Set<Object> controllers() {
            return ImmutableSet.of(new PostController());
        }
    }

}
