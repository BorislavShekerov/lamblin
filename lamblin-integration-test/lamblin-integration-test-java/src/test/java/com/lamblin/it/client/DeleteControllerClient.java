/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.it.client;

import com.lamblin.it.model.ResponseEntity;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.lamblin.it.client.ClientUtils.createObjectMapper;
import static com.lamblin.it.client.ClientUtils.executeRequest;
import static com.lamblin.it.model.EndpointsKt.*;
import static com.lamblin.it.model.TestUtilsKt.*;

public final class DeleteControllerClient {

    public static final DeleteControllerClient INSTANCE = new DeleteControllerClient();
    private final DeleteControllerApi client;

    private DeleteControllerClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(createObjectMapper()))
                .baseUrl(getServerBaseUrl())
                .build();

        this.client = retrofit.create(DeleteControllerApi.class);
    }

    public Response<ResponseEntity> callSimpleDeleteNoParamsEndpoint() {
        return executeRequest(client::callSimpleDeleteNoParamsEndpoint);
    }

    public Response<ResponseEntity> callSingleQueryParamEndpoint(String queryParam) {
        return executeRequest(() -> client.callSingleQueryParamEndpoint(queryParam));
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

    private interface DeleteControllerApi {

        @DELETE(SIMPLE_DELETE_ENDPOINT)
        Call<ResponseEntity> callSimpleDeleteNoParamsEndpoint();

        @DELETE(QUERY_PARAM_DELETE_ENDPOINT)
        Call<ResponseEntity> callSingleQueryParamEndpoint(@Query(QUERY_PARAM_1) String queryParam);

        @DELETE(QUERY_PARAM_DELETE_ENDPOINT)
        Call<ResponseEntity> callQueryParamEndpoint(@Query(QUERY_PARAM_1) String queryParam1,
                                                    @Query(QUERY_PARAM_2) String queryParam2);

        @DELETE(SINGLE_PATH_PARAM_DELETE_ENDPOINT)
        Call<ResponseEntity> callSinglePathParamEndpoint(@Path(PATH_PARAM_1) String pathParam1);

        @DELETE(MULTIPLE_PATH_PARAM_DELETE_ENDPOINT)
        Call<ResponseEntity> callMultiPathParamEndpoint(@Path(PATH_PARAM_1) String pathParam1,
                                                        @Path(PATH_PARAM_2) String pathParam2);

        @DELETE(MULTIPLE_PATH_PARAM_DELETE_ENDPOINT)
        Call<ResponseEntity> callMultiPathParamEndpoint(@Path(PATH_PARAM_1) String pathParam1,
                                                        @Path(PATH_PARAM_2) String pathParam2,
                                                        @Query(QUERY_PARAM_1) String queryParam1);
    }
}
