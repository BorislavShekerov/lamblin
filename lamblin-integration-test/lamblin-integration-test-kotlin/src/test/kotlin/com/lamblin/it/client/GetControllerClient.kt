/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.it.client

import com.lamblin.it.model.*
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

object GetControllerClient {

    private val client: GetControllerApi

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(createObjectMapper()))
            .client(
                OkHttpClient.Builder()
                    .readTimeout(1, TimeUnit.HOURS)
                    .build())
            .baseUrl(getServerBaseUrl())
            .build()

        client = retrofit.create(GetControllerApi::class.java)
    }

    fun callSimpleGetNoParamsEndpoint() = client.callSimpleGetNoParamsEndpoint().execute()

    fun callSingleQueryParamEndpoint(queryParam: String) = client.callQuerySingleParamEndpoint(queryParam).execute()

    fun callQueryParamDefaultValueEndpoint() = client.callQueryParamDefaultValueEndpoint().execute()

    fun callMultiQueryParamEndpoint(queryParam1: String, queryParam2: String) =
        client.callMultiQueryParamEndpoint(queryParam1, queryParam2).execute()

    fun callMultiKeyQueryParamEndpoint(queryParamValue1: String, queryParamValue2: String) =
        client.callMultiKeyQueryParamEndpoint(
            "${getServerBaseUrl()}$QUERY_PARAM_MULTI_KEY_GET_ENDPOINT?$QUERY_PARAM_1=$queryParamValue1&$QUERY_PARAM_1=$queryParamValue2").execute()

    fun callSinglePathParamEndpoint(pathParam: String) = client.callSinglePathParamEndpoint(pathParam).execute()

    fun callMultiPathParamEndpoint(pathParam1: String, pathParam2: String) =
        client.callMultiPathParamEndpoint(pathParam1, pathParam2).execute()

    fun callMultiPathParamWithQueryParamEndpoint(
        queryParam: String,
        pathParam1: String,
        pathParam2: String
    ) = client.callMultiPathParamEndpoint(pathParam1, pathParam2, queryParam).execute()

    fun callCustomStatusCodeEndpoint() = client.callCustomStatusCodeEndpoint().execute()

    fun callUnknownEndpoint() = client.callUnknownEndpoint().execute()

    private interface GetControllerApi {

        @GET(SIMPLE_GET_ENDPOINT)
        fun callSimpleGetNoParamsEndpoint(): Call<ResponseEntity>

        @GET(QUERY_PARAM_GET_ENDPOINT)
        fun callQuerySingleParamEndpoint(@Query(QUERY_PARAM_1) queryParam1: String): Call<ResponseEntity>

        @GET(QUERY_PARAM_GET_ENDPOINT)
        fun callMultiQueryParamEndpoint(
            @Query(QUERY_PARAM_1) queryParam1: String,
            @Query(QUERY_PARAM_2) queryParam2: String): Call<ResponseEntity>

        @GET
        fun callMultiKeyQueryParamEndpoint(
            @Url url:String): Call<ResponseEntity>

        @GET(QUERY_PARAM_DEFAULT_VALUE_GET_ENDPOINT)
        fun callQueryParamDefaultValueEndpoint(): Call<ResponseEntity>

        @GET(SINGLE_PATH_PARAM_GET_ENDPOINT)
        fun callSinglePathParamEndpoint(@Path(PATH_PARAM_1) pathParam1: String): Call<ResponseEntity>

        @GET(MULTI_PATH_PARAM_GET_ENDPOINT)
        fun callMultiPathParamEndpoint(
            @Path(PATH_PARAM_1) pathParam1: String,
            @Path(PATH_PARAM_2) pathParam2: String
        ): Call<ResponseEntity>

        @GET(MULTI_PATH_PARAM_GET_ENDPOINT)
        fun callMultiPathParamEndpoint(
            @Path(PATH_PARAM_1) pathParam1: String,
            @Path(PATH_PARAM_2) pathParam2: String,
            @Query(QUERY_PARAM_1) queryParam1: String
        ): Call<ResponseEntity>

        @GET(CUSTOM_STATUS_CODE_GET_ENDPOINT)
        fun callCustomStatusCodeEndpoint(): Call<Void>

        @GET("/unknown")
        fun callUnknownEndpoint(): Call<Void>

    }
}