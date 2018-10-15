package com.lamblin.it;

import com.google.common.collect.ImmutableSet;

import com.lamblin.it.controller.PatchController;
import com.lamblin.it.client.PatchControllerClient;
import com.lamblin.it.model.ExampleRequestBody;
import com.lamblin.local.runner.LamblinLocalRunner;
import com.lamblin.test.config.LamblinTestConfig;
import com.lamblin.test.config.annotation.LamblinTestRunnerConfig;
import com.lamblin.test.junit4.JUnit4LamblinTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static com.lamblin.it.model.EndpointsKt.MULTI_PATH_PARAM_PATCH_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.QUERY_PARAM_PATCH_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SIMPLE_PATCH_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SIMPLE_REQUEST_BODY_PATCH_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SINGLE_PATH_PARAM_PATCH_ENDPOINT;
import static com.lamblin.it.model.TestUtilsKt.runRequestAndVerifyResponse;
import static java.text.MessageFormat.format;

@RunWith(JUnit4LamblinTestRunner.class)
@LamblinTestRunnerConfig(testConfigClass = PatchControllerTest.TestConfiguration.class)
public class PatchControllerTest {

    private static final PatchControllerClient client = PatchControllerClient.INSTANCE;

    @Test
    public void shouldHandlePatchRequestsWithNoParams() {
        runRequestAndVerifyResponse(
                client::callSimplePatchNoParamsEndpoint,
                SIMPLE_PATCH_ENDPOINT);
    }

    @Test
    public void shouldHandlePatchRequestsWithSingleQueryParam() {
        String queryParamValue = "value";

        runRequestAndVerifyResponse(
                () -> client.callSingleQueryParamEndpoint(queryParamValue),
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
                () -> client.callMultiQueryParamEndpoint(queryParam1Value, queryParam2Value),
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
                () -> client.callSinglePathParamEndpoint(pathParamValue),
                format(
                        "{0}-{1}",
                        SINGLE_PATH_PARAM_PATCH_ENDPOINT,
                        pathParamValue));
    }

    @Test
    public void shouldHandlePatchRequestsWithMultiplePathParams() {
        String pathParamValue1 = "value1";
        String pathParamValue2 = "value2";

        runRequestAndVerifyResponse(
                () -> client.callMultiPathParamEndpoint(pathParamValue1, pathParamValue2),
                format(
                        "{0}-{1},{2}",
                        MULTI_PATH_PARAM_PATCH_ENDPOINT,
                        pathParamValue1,
                        pathParamValue2));
    }

    @Test
    public void shouldHandlePatchRequestsWithMultiplePathParamsAndQueryParams() {
        String queryParamValue = "queryParamValue";
        String pathParamValue1 = "value1";
        String pathParamValue2 = "value2";

        runRequestAndVerifyResponse(
                () -> client
                        .callMultiPathParamEndpointWithQueryParam(queryParamValue, pathParamValue1, pathParamValue2),
                format(
                        "{0}-{1},{2},{3}",
                        MULTI_PATH_PARAM_PATCH_ENDPOINT,
                        queryParamValue,
                        pathParamValue1,
                        pathParamValue2));
    }


    @Test
    public void shouldHandlePatchRequestsWithRequestBody() {
        String bodyContent = "test";
        ExampleRequestBody requestBody = new ExampleRequestBody(bodyContent);

        runRequestAndVerifyResponse(
                () -> client.callRequestBodyEndpoint(requestBody),
                format("{0}-{1}", SIMPLE_REQUEST_BODY_PATCH_ENDPOINT, bodyContent));
    }

    public static class TestConfiguration implements LamblinTestConfig {

        @Override
        public Set<Object> controllers() {
            return ImmutableSet.of(new PatchController());
        }
    }
}
