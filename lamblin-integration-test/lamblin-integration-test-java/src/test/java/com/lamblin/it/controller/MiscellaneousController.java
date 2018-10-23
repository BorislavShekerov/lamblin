package com.lamblin.it.controller;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.lamblin.core.model.HttpMethod;
import com.lamblin.core.model.HttpResponse;
import com.lamblin.core.model.StatusCode;
import com.lamblin.core.model.annotation.Controller;
import com.lamblin.core.model.annotation.Endpoint;
import com.lamblin.core.model.annotation.Header;
import com.lamblin.core.security.AccessControl;
import com.lamblin.core.security.RequestAuthorizer;
import com.lamblin.it.model.ResponseEntity;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static com.lamblin.it.model.EndpointsKt.*;
import static com.lamblin.it.model.TestUtilsKt.AUTHORIZATION_HEADER;
import static com.lamblin.it.model.TestUtilsKt.AUTHORIZED_ROLE;
import static com.lamblin.it.model.TestUtilsKt.UNAUTHORIZED_ROLE;
import static java.text.MessageFormat.format;

@Controller
public class MiscellaneousController {

    @Endpoint(path = HEADER_GET_ENDPOINT, method = HttpMethod.GET)
    public HttpResponse<ResponseEntity> headerPath(@Header(AUTHORIZATION_HEADER) String authorizationHeader) {
        return HttpResponse.ok(
                new ResponseEntity(
                        format(
                                "{0}-{1}",
                                HEADER_GET_ENDPOINT,
                                authorizationHeader)));
    }

    @Endpoint(path = API_GATEWAY_REQUEST_EVENT_GET_ENDPOINT, method = HttpMethod.GET)
    public HttpResponse<ResponseEntity> apiGatewayRequestEventEndpoint(
            APIGatewayProxyRequestEvent apiGatewayRequestEvent) {

        return HttpResponse.ok(
                new ResponseEntity(
                        format(
                                "{0}-{1}",
                                API_GATEWAY_REQUEST_EVENT_GET_ENDPOINT,
                                apiGatewayRequestEvent.getPath())));
    }

    @Endpoint(path = CUSTOM_STATUS_CODE_GET_ENDPOINT, method = HttpMethod.GET)
    public HttpResponse<Object> customStatusCodeEndpoint() {
        return HttpResponse.withCode(StatusCode.ACCEPTED);
    }

    @AccessControl(roles = { AUTHORIZED_ROLE }, authorizer = Authorizer.class)
    @Endpoint(path = ACCESS_CONTROL_AUTHORIZED_GET_ENDPOINT, method = HttpMethod.GET)
    public HttpResponse<Void> accessControlAuthorized() {
        return HttpResponse.ok();
    }

    @AccessControl(roles = { AUTHORIZED_ROLE }, authorizer = Authorizer.class)
    @Endpoint(path = ACCESS_CONTROL_UNAUTHORIZED_GET_ENDPOINT, method = HttpMethod.GET)
    public HttpResponse<Void> accessControlUnauthorized() {
        return HttpResponse.ok();
    }

    public static class Authorizer implements RequestAuthorizer {

        @Override
        public boolean isRequestAuthorized(@NotNull String[] roles, @NotNull APIGatewayProxyRequestEvent request) {
            return request.getPath().equals(ACCESS_CONTROL_AUTHORIZED_GET_ENDPOINT);
        }
    }
}
