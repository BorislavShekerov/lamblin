package com.lamblin.it;

import com.google.common.collect.ImmutableSet;
import com.lamblin.core.model.StatusCode;
import com.lamblin.it.client.MiscellaneousControllerClient;
import com.lamblin.it.controller.MiscellaneousController;
import com.lamblin.test.config.LamblinTestConfig;
import com.lamblin.test.config.annotation.LamblinTestRunnerConfig;
import com.lamblin.test.junit4.JUnit4LamblinTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Set;

import static com.lamblin.it.model.EndpointsKt.HEADER_GET_ENDPOINT;
import static com.lamblin.it.model.TestUtilsKt.AUTHORIZATION_HEADER_VALUE;
import static com.lamblin.it.model.TestUtilsKt.runRequestAndVerifyResponse;
import static java.text.MessageFormat.format;

@RunWith(JUnit4LamblinTestRunner.class)
@LamblinTestRunnerConfig(testConfigClass = MiscellaneousControllerTest.TestConfiguration.class)

public class MiscellaneousControllerTest {

    private static final MiscellaneousControllerClient client = MiscellaneousControllerClient.INSTANCE;

    @Test
    public void shouldHandlePathWithHeaderInjection() {
        runRequestAndVerifyResponse(
                () -> client.callHeaderInjectionEndpoint(AUTHORIZATION_HEADER_VALUE),
                format(
                        "{0}-{1}",
                        HEADER_GET_ENDPOINT,
                        AUTHORIZATION_HEADER_VALUE));
    }

    @Test
    public void shouldReturn404ForUnknownRoutes() {
        runRequestAndVerifyResponse(
                client::callUnknownEndpoint,
                null,
                StatusCode.NOT_FOUND.getCode());
    }

    @Test
    public void shouldReturnStatusCodeReturnedFromEndpoint() {
        runRequestAndVerifyResponse(
                client::callCustomStatusCodeEndpoint,
                null,
                StatusCode.ACCEPTED.getCode());
    }

    public static class TestConfiguration implements LamblinTestConfig {

        @Override
        public Set<Object> controllers() {
            return ImmutableSet.of(new MiscellaneousController());
        }
    }

}
