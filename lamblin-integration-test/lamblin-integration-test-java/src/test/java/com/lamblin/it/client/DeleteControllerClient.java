/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.it.client;

import com.lamblin.it.model.ResponseEntity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.DELETE;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.lamblin.it.client.ClientUtils.createObjectMapper;
import static com.lamblin.it.model.EndpointsKt.MULTIPLE_PATH_PARAM_DELETE_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.QUERY_PARAM_DELETE_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SIMPLE_DELETE_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SINGLE_PATH_PARAM_DELETE_ENDPOINT;
import static com.lamblin.it.model.TestUtilsKt.PATH_PARAM_1;
import static com.lamblin.it.model.TestUtilsKt.PATH_PARAM_2;
import static com.lamblin.it.model.TestUtilsKt.QUERY_PARAM_1;
import static com.lamblin.it.model.TestUtilsKt.QUERY_PARAM_2;
import static com.lamblin.it.model.TestUtilsKt.getServerBaseUrl;

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
        try {
            return client.callSimpleDeleteNoParamsEndpoint().execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response<ResponseEntity> callSingleQueryParamEndpoint(String queryParam) {
        try {
            return client.callSingleQueryParamEndpoint(queryParam).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response<ResponseEntity> callMultiQueryParamEndpoint(String queryParam1, String queryParam2) {
        try {
            return client.callQueryParamEndpoint(queryParam1, queryParam2).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response<ResponseEntity> callSinglePathParamEndpoint(String pathParam) {
        try {
            return client.callSinglePathParamEndpoint(pathParam).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response<ResponseEntity> callMultiPathParamEndpoint(String pathParam1, String pathParam2) {
        try {
            return client.callMultiPathParamEndpoint(pathParam1, pathParam2).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response<ResponseEntity> callMultiPathParamWithQueryParamEndpoint(
            String queryParam,
            String pathParam1,
            String pathParam2) {

        try {
            return client.callMultiPathParamEndpoint(pathParam1, pathParam2, queryParam).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
