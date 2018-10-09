package com.lamblin.it.controller.client;

import com.lamblin.it.model.ResponseEntity;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.lamblin.it.controller.client.ClientUtils.createObjectMapper;
import static com.lamblin.it.model.EndpointsKt.CUSTOM_STATUS_CODE_GET_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.MULTI_PATH_PARAM_GET_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.QUERY_PARAM_GET_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SIMPLE_GET_ENDPOINT;
import static com.lamblin.it.model.EndpointsKt.SINGLE_PATH_PARAM_GET_ENDPOINT;
import static com.lamblin.it.model.TestUtilsKt.PATH_PARAM_1;
import static com.lamblin.it.model.TestUtilsKt.PATH_PARAM_2;
import static com.lamblin.it.model.TestUtilsKt.QUERY_PARAM_1;
import static com.lamblin.it.model.TestUtilsKt.QUERY_PARAM_2;
import static com.lamblin.it.model.TestUtilsKt.getServerBaseUrl;

public class GetControllerClient {

    public static final GetControllerClient INSTANCE = new GetControllerClient();
    private final GetControllerApi client;

    private GetControllerClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(createObjectMapper()))
                .baseUrl(getServerBaseUrl())
                .build();

        this.client = retrofit.create(GetControllerApi.class);
    }

    public Response<ResponseEntity> callSimpleGetNoParamsEndpoint() {
        try {
            return client.callSimpleGetNoParamsEndpoint().execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response<ResponseEntity> callSingleQueryParamEndpoint(String queryParam) {
        try {
            return client.callQueryParamEndpoint(queryParam, null).execute();
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

    public Response<Void> callCustomStatusCodeEndpoint() {
        try {
            return client.callCustomStatusCodeEndpoint().execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response callUnknownEndpoint() {
        try {
            return client.callUnknownEndpoint().execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private interface GetControllerApi {

        @GET(SIMPLE_GET_ENDPOINT)
        Call<ResponseEntity> callSimpleGetNoParamsEndpoint();

        @GET(QUERY_PARAM_GET_ENDPOINT)
        Call<ResponseEntity> callQueryParamEndpoint(@Query(QUERY_PARAM_1) String queryParam1,
                                                    @Query(QUERY_PARAM_2) String queryParam2);

        @GET(SINGLE_PATH_PARAM_GET_ENDPOINT)
        Call<ResponseEntity> callSinglePathParamEndpoint(@Path(PATH_PARAM_1) String pathParam1);

        @GET(MULTI_PATH_PARAM_GET_ENDPOINT)
        Call<ResponseEntity> callMultiPathParamEndpoint(@Path(PATH_PARAM_1) String pathParam1,
                                                        @Path(PATH_PARAM_2) String pathParam2);

        @GET(MULTI_PATH_PARAM_GET_ENDPOINT)
        Call<ResponseEntity> callMultiPathParamEndpoint(@Path(PATH_PARAM_1) String pathParam1,
                                                        @Path(PATH_PARAM_2) String pathParam2,
                                                        @Query(QUERY_PARAM_1) String queryParam1);

        @GET(CUSTOM_STATUS_CODE_GET_ENDPOINT)
        Call<Void> callCustomStatusCodeEndpoint();

        @GET("/unknown")
        Call<Void> callUnknownEndpoint();

    }
}
