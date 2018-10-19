package com.lamblin.it.controller;

import com.lamblin.core.model.HttpMethod;
import com.lamblin.core.model.HttpResponse;
import com.lamblin.core.model.StatusCode;
import com.lamblin.core.model.annotation.Controller;
import com.lamblin.core.model.annotation.Endpoint;
import com.lamblin.core.model.annotation.Header;
import com.lamblin.it.model.ResponseEntity;

import static com.lamblin.it.model.EndpointsKt.CUSTOM_STATUS_CODE_GET_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.HEADER_GET_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.MULTI_PATH_PARAM_GET_ENDPOINT;
import static com.lamblin.it.model.TestUtilsKt.AUTHORIZATION_HEADER;
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

    @Endpoint(path = CUSTOM_STATUS_CODE_GET_ENDPOINT, method = HttpMethod.GET)
    public HttpResponse<Object> customStatusCodeEndpoint() {
        return HttpResponse.withCode(StatusCode.ACCEPTED);
    }

}
