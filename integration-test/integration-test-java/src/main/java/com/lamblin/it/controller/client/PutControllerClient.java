package com.lamblin.it.controller.client;

import com.lamblin.it.model.ExampleRequestBody;
import com.lamblin.it.model.ResponseEntity;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.io.IOException;

import static com.lamblin.it.controller.client.ClientUtils.createObjectMapper;
import static com.lamblin.it.model.EndpointsKt.*;
import static com.lamblin.it.model.TestUtilsKt.*;

public class PutControllerClient {

    public static final PutControllerClient INSTANCE = new PutControllerClient();
    private final PutControllerApi client;

    private PutControllerClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(createObjectMapper()))
                .baseUrl(getServerBaseUrl())
                .build();

        this.client = retrofit.create(PutControllerApi.class);
    }

    public Response<ResponseEntity> callSimplePostNoParamsEndpoint() {
        try {
            return client.callSimplePostNoParamsEndpoint().execute();
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
            return client.callMultiQueryParamEndpoint(queryParam1, queryParam2).execute();
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

    public Response<ResponseEntity> callMultiPathParamEndpointWithQueryParamEndpoint(
            String queryParam,
            String pathParam1,
            String pathParam2) {

        try {
            return client.callMultiPathParamEndpoint(pathParam1, pathParam2, queryParam).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response<ResponseEntity> callRequestBodyEndpoint(ExampleRequestBody exampleRequestBod) {

        try {
            return client.callRequestBodyEndpoint(exampleRequestBod).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private interface PutControllerApi {

        @PUT(SIMPLE_PUT_ENDPOINT)
        Call<ResponseEntity> callSimplePostNoParamsEndpoint();

        @PUT(QUERY_PARAM_PUT_ENDPOINT)
        Call<ResponseEntity> callSingleQueryParamEndpoint(@Query(QUERY_PARAM_1) String queryParam);

        @PUT(QUERY_PARAM_PUT_ENDPOINT)
        Call<ResponseEntity> callMultiQueryParamEndpoint(@Query(QUERY_PARAM_1) String queryParam1,
                                                         @Query(QUERY_PARAM_2) String queryParam2);

        @PUT(SINGLE_PATH_PARAM_PUT_ENDPOINT)
        Call<ResponseEntity> callSinglePathParamEndpoint(@Path(PATH_PARAM_1) String pathParam1);

        @PUT(MULTI_PATH_PARAM_PUT_ENDPOINT)
        Call<ResponseEntity> callMultiPathParamEndpoint(@Path(PATH_PARAM_1) String pathParam1,
                                                        @Path(PATH_PARAM_2) String pathParam2);

        @PUT(MULTI_PATH_PARAM_PUT_ENDPOINT)
        Call<ResponseEntity> callMultiPathParamEndpoint(@Path(PATH_PARAM_1) String pathParam1,
                                                        @Path(PATH_PARAM_2) String pathParam2,
                                                        @Query(QUERY_PARAM_1) String queryParam1);

        @PUT(SIMPLE_REQUEST_BODY_PUT_ENDPOINT)
        Call<ResponseEntity> callRequestBodyEndpoint(@Body ExampleRequestBody exampleRequestBody);

    }

}
