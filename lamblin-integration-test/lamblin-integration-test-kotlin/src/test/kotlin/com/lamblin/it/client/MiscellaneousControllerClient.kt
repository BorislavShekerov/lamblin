package com.lamblin.it.client

import com.lamblin.it.model.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import java.util.concurrent.TimeUnit

object MiscellaneousControllerClient {

    private val client: MiscellaneousControllerApi

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(createObjectMapper()))
            .client(
                OkHttpClient.Builder()
                    .readTimeout(1, TimeUnit.HOURS)
                    .build())
            .baseUrl(getServerBaseUrl())
            .build()

        client = retrofit.create(MiscellaneousControllerApi::class.java)
    }

    fun callHeaderInjectionEndpoint(authorizationHeaderValue: String) =
        client.callHeaderInjectionEndpoint(authorizationHeaderValue).execute()

    fun callCustomStatusCodeEndpoint() = client.callCustomStatusCodeEndpoint().execute()

    fun callUnknownEndpoint() = client.callUnknownEndpoint().execute()

    fun callApiGatewayRequestEventEndpoint() = client.callApiGatewayRequestInjectionEndpoint().execute()

    fun callAccessControlAuthorizedEndpoint() = client.callAccessControlAuthorizedEndpoint().execute()

    fun callAccessControlUnauthorizedEndpoint() = client.callAccessControlUnauthorizedEndpoint().execute()

    private interface MiscellaneousControllerApi {
        @GET(HEADER_GET_ENDPOINT)
        fun callHeaderInjectionEndpoint(@Header(AUTHORIZATION_HEADER) header: String): Call<ResponseEntity>

        @GET(API_GATEWAY_REQUEST_EVENT_GET_ENDPOINT)
        fun callApiGatewayRequestInjectionEndpoint(): Call<ResponseEntity>

        @GET(CUSTOM_STATUS_CODE_GET_ENDPOINT)
        fun callCustomStatusCodeEndpoint(): Call<Void>

        @GET(ACCESS_CONTROL_AUTHORIZED_GET_ENDPOINT)
        fun callAccessControlAuthorizedEndpoint(): Call<Void>

        @GET(ACCESS_CONTROL_UNAUTHORIZED_GET_ENDPOINT)
        fun callAccessControlUnauthorizedEndpoint(): Call<Void>

        @GET("/unknown")
        fun callUnknownEndpoint(): Call<Void>
    }
}