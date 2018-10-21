package com.lamblin.it.client

import com.lamblin.it.model.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

object MiscellaneousControllerClient {

    private val client: MiscellaneousControllerApi

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(createObjectMapper()))
            .baseUrl(getServerBaseUrl())
            .build()

        client = retrofit.create(MiscellaneousControllerApi::class.java)
    }

    fun callHeaderInjectionEndpoint(authorizationHeaderValue: String) =
        client.callHeaderInjectionEndpoint(authorizationHeaderValue).execute()

    fun callCustomStatusCodeEndpoint() = client.callCustomStatusCodeEndpoint().execute()

    fun callUnknownEndpoint() = client.callUnknownEndpoint().execute()

    private interface MiscellaneousControllerApi {
        @GET(HEADER_GET_ENDPOINT)
        fun callHeaderInjectionEndpoint(@Header(AUTHORIZATION_HEADER) header: String): Call<ResponseEntity>

        @GET(CUSTOM_STATUS_CODE_GET_ENDPOINT)
        fun callCustomStatusCodeEndpoint(): Call<Void>

        @GET("/unknown")
        fun callUnknownEndpoint(): Call<Void>
    }
}