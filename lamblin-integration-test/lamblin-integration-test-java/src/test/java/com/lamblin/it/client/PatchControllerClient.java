/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.it.client;

import com.lamblin.it.model.ExampleRequestBody;
import com.lamblin.it.model.ResponseEntity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.concurrent.TimeUnit;

import static com.lamblin.it.client.ClientUtils.createObjectMapper;
import static com.lamblin.it.client.ClientUtils.executeRequest;
import static com.lamblin.it.model.EndpointsKt.*;
import static com.lamblin.it.model.TestUtilsKt.*;

public class PatchControllerClient {

    public static final PatchControllerClient INSTANCE = new PatchControllerClient();
    private final PatchControllerApi client;

    private PatchControllerClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(createObjectMapper()))
                .client(
                        new OkHttpClient.Builder()
                                .readTimeout(1, TimeUnit.HOURS)
                                .build())
                .baseUrl(getServerBaseUrl())
                .build();

        this.client = retrofit.create(PatchControllerApi.class);
    }

    public Response<ResponseEntity> callSimplePatchNoParamsEndpoint() {
        return executeRequest(client::callSimplePatchNoParamsEndpoint);
    }

    public Response<ResponseEntity> callSingleQueryParamEndpoint(String queryParam) {
        return executeRequest(() ->  client.callSingleQueryParamEndpoint(queryParam));
    }

    public Response<ResponseEntity> callMultiQueryParamEndpoint(String queryParam1, String queryParam2) {
        return executeRequest(() -> client.callMultiQueryParamEndpoint(queryParam1, queryParam2));
    }

    public Response<ResponseEntity> callSinglePathParamEndpoint(String pathParam) {
        return executeRequest(() -> client.callSinglePathParamEndpoint(pathParam));
    }

    public Response<ResponseEntity> callMultiPathParamEndpoint(String pathParam1, String pathParam2) {
        return executeRequest(() -> client.callMultiPathParamEndpoint(pathParam1, pathParam2));
    }

    public Response<ResponseEntity> callMultiPathParamEndpointWithQueryParam(
            String queryParam,
            String pathParam1,
            String pathParam2) {

        return executeRequest(() -> client.callMultiPathParamEndpoint(pathParam1, pathParam2, queryParam));
    }

    public Response<ResponseEntity> callRequestBodyEndpoint(ExampleRequestBody exampleRequestBod) {
        return executeRequest(() -> client.callRequestBodyEndpoint(exampleRequestBod));
    }

    private interface PatchControllerApi {

        @PATCH(SIMPLE_PATCH_ENDPOINT)
        Call<ResponseEntity> callSimplePatchNoParamsEndpoint();

        @PATCH(QUERY_PARAM_PATCH_ENDPOINT)
        Call<ResponseEntity> callSingleQueryParamEndpoint(@Query(QUERY_PARAM_1) String queryParam);

        @PATCH(QUERY_PARAM_PATCH_ENDPOINT)
        Call<ResponseEntity> callMultiQueryParamEndpoint(@Query(QUERY_PARAM_1) String queryParam1,
                                                         @Query(QUERY_PARAM_2) String queryParam2);

        @PATCH(SINGLE_PATH_PARAM_PATCH_ENDPOINT)
        Call<ResponseEntity> callSinglePathParamEndpoint(@Path(PATH_PARAM_1) String pathParam1);

        @PATCH(MULTI_PATH_PARAM_PATCH_ENDPOINT)
        Call<ResponseEntity> callMultiPathParamEndpoint(@Path(PATH_PARAM_1) String pathParam1,
                                                        @Path(PATH_PARAM_2) String pathParam2);

        @PATCH(MULTI_PATH_PARAM_PATCH_ENDPOINT)
        Call<ResponseEntity> callMultiPathParamEndpoint(@Path(PATH_PARAM_1) String pathParam1,
                                                        @Path(PATH_PARAM_2) String pathParam2,
                                                        @Query(QUERY_PARAM_1) String queryParam1);

        @PATCH(SIMPLE_REQUEST_BODY_PATCH_ENDPOINT)
        Call<ResponseEntity> callRequestBodyEndpoint(@Body ExampleRequestBody exampleRequestBody);

    }
}
