package com.lamblin.it.controller.client;

import com.lamblin.it.model.ExampleRequestBody;
import com.lamblin.it.model.ResponseEntity;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.PATCH;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.io.IOException;

import static com.lamblin.it.controller.client.ClientUtils.createObjectMapper;
import static com.lamblin.it.model.EndpointsKt.*;
import static com.lamblin.it.model.TestUtilsKt.*;

public class PatchControllerClient {

    public static final PatchControllerClient INSTANCE = new PatchControllerClient();
    private final PatchControllerApi client;

    private PatchControllerClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(createObjectMapper()))
                .baseUrl(getServerBaseUrl())
                .build();

        this.client = retrofit.create(PatchControllerApi.class);
    }

    public Response<ResponseEntity> callSimplePatchNoParamsEndpoint() {
        try {
            return client.callSimplePatchNoParamsEndpoint().execute();
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

    public Response<ResponseEntity> callMultiPathParamEndpointWithQueryParam(
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
