package com.lamblin.it.client;

import com.lamblin.it.model.ResponseEntity;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

import java.util.concurrent.TimeUnit;

import static com.lamblin.it.client.ClientUtils.createObjectMapper;
import static com.lamblin.it.client.ClientUtils.executeRequest;
import static com.lamblin.it.model.EndpointsKt.*;
import static com.lamblin.it.model.TestUtilsKt.AUTHORIZATION_HEADER;
import static com.lamblin.it.model.TestUtilsKt.getServerBaseUrl;

public class MiscellaneousControllerClient {

    public static final MiscellaneousControllerClient INSTANCE = new MiscellaneousControllerClient();
    private final MiscellaneousControllerClient.MiscellaneousControllerApi client;

    private MiscellaneousControllerClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(createObjectMapper()))
                .client(
                        new OkHttpClient.Builder()
                                .readTimeout(1, TimeUnit.HOURS)
                                .build())
                .baseUrl(getServerBaseUrl())
                .build();

        this.client = retrofit.create(MiscellaneousControllerClient.MiscellaneousControllerApi.class);
    }

    public Response<ResponseEntity> callHeaderInjectionEndpoint(String authorizationHeaderValue) {
        return executeRequest(() -> client.callHeaderInjectionEndpoint(authorizationHeaderValue));
    }

    public Response<Void> callCustomStatusCodeEndpoint() {
        return executeRequest(client::callCustomStatusCodeEndpoint);
    }

    public Response callUnknownEndpoint() {
        return executeRequest(client::callUnknownEndpoint);
    }

    public Response callApiGatewayRequestEventEndpoint() {
        return executeRequest(client::callApiGatewayRequestInjectionEndpoint);
    }

    public Response callAccessControlAuthorizedEndpoint() {
        return executeRequest(client::callAccessControlAuthorizedEndpoint);
    }

    public Response callAccessControlUnauthorizedEndpoint() {
        return executeRequest(client::callAccessControlUnauthorizedEndpoint);
    }

    private interface MiscellaneousControllerApi {

        @GET(HEADER_GET_ENDPOINT)
        Call<ResponseEntity> callHeaderInjectionEndpoint(@Header(AUTHORIZATION_HEADER) String header);

        @GET(API_GATEWAY_REQUEST_EVENT_GET_ENDPOINT)
        Call<ResponseEntity> callApiGatewayRequestInjectionEndpoint();

        @GET(CUSTOM_STATUS_CODE_GET_ENDPOINT)
        Call<Void> callCustomStatusCodeEndpoint();

        @GET(ACCESS_CONTROL_AUTHORIZED_GET_ENDPOINT)
        Call<Void> callAccessControlAuthorizedEndpoint();

        @GET(ACCESS_CONTROL_UNAUTHORIZED_GET_ENDPOINT)
        Call<Void> callAccessControlUnauthorizedEndpoint();

        @GET("/unknown")
        Call<Void> callUnknownEndpoint();
    }
}
