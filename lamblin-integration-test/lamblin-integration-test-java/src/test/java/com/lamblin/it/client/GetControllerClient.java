/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.it.client;

import com.lamblin.it.model.ResponseEntity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.concurrent.TimeUnit;

import static com.lamblin.it.client.ClientUtils.createObjectMapper;
import static com.lamblin.it.client.ClientUtils.executeRequest;
import static com.lamblin.it.model.EndpointsKt.*;
import static com.lamblin.it.model.TestUtilsKt.*;

public class GetControllerClient {

    public static final GetControllerClient INSTANCE = new GetControllerClient();
    private final GetControllerApi client;

    private GetControllerClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(createObjectMapper()))
                .client(
                        new OkHttpClient.Builder()
                                .readTimeout(1, TimeUnit.HOURS)
                                .build())
                .baseUrl(getServerBaseUrl())
                .build();

        this.client = retrofit.create(GetControllerApi.class);
    }

    public Response<ResponseEntity> callSimpleGetNoParamsEndpoint() {
        return executeRequest(client::callSimpleGetNoParamsEndpoint);
    }

    public Response<ResponseEntity> callQueryParamDefaultValueEndpoint() {
        return executeRequest(client::callQueryParamDefaultValueEndpoint);
    }

    public Response<ResponseEntity> callSingleQueryParamEndpoint(String queryParam) {
        return executeRequest(() -> client.callQueryParamEndpoint(queryParam, null));
    }

    public Response<ResponseEntity> callMultiQueryParamEndpoint(String queryParam1, String queryParam2) {
        return executeRequest(() -> client.callQueryParamEndpoint(queryParam1, queryParam2));
    }

    public Response<ResponseEntity> callSinglePathParamEndpoint(String pathParam) {
        return executeRequest(() -> client.callSinglePathParamEndpoint(pathParam));
    }

    public Response<ResponseEntity> callMultiPathParamEndpoint(String pathParam1, String pathParam2) {
        return executeRequest(() -> client.callMultiPathParamEndpoint(pathParam1, pathParam2));
    }

    public Response<ResponseEntity> callMultiPathParamWithQueryParamEndpoint(
            String queryParam,
            String pathParam1,
            String pathParam2) {

        return executeRequest(() -> client.callMultiPathParamEndpoint(pathParam1, pathParam2, queryParam));
    }



    private interface GetControllerApi {

        @GET(SIMPLE_GET_ENDPOINT)
        Call<ResponseEntity> callSimpleGetNoParamsEndpoint();

        @GET(QUERY_PARAM_GET_ENDPOINT)
        Call<ResponseEntity> callQueryParamEndpoint(@Query(QUERY_PARAM_1) String queryParam1,
                                                    @Query(QUERY_PARAM_2) String queryParam2);

        @GET(QUERY_PARAM_DEFAULT_VALUE_GET_ENDPOINT)
        Call<ResponseEntity> callQueryParamDefaultValueEndpoint();

        @GET(SINGLE_PATH_PARAM_GET_ENDPOINT)
        Call<ResponseEntity> callSinglePathParamEndpoint(@Path(PATH_PARAM_1) String pathParam1);

        @GET(MULTI_PATH_PARAM_GET_ENDPOINT)
        Call<ResponseEntity> callMultiPathParamEndpoint(@Path(PATH_PARAM_1) String pathParam1,
                                                        @Path(PATH_PARAM_2) String pathParam2);

        @GET(MULTI_PATH_PARAM_GET_ENDPOINT)
        Call<ResponseEntity> callMultiPathParamEndpoint(@Path(PATH_PARAM_1) String pathParam1,
                                                        @Path(PATH_PARAM_2) String pathParam2,
                                                        @Query(QUERY_PARAM_1) String queryParam1);

    }
}
