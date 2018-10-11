/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.it.controller.client

import com.lamblin.it.model.CUSTOM_STATUS_CODE_GET_ENDPOINT
import com.lamblin.it.model.MULTI_PATH_PARAM_GET_ENDPOINT
import com.lamblin.it.model.PATH_PARAM_1
import com.lamblin.it.model.PATH_PARAM_2
import com.lamblin.it.model.QUERY_PARAM_1
import com.lamblin.it.model.QUERY_PARAM_2
import com.lamblin.it.model.QUERY_PARAM_GET_ENDPOINT
import com.lamblin.it.model.ResponseEntity
import com.lamblin.it.model.SIMPLE_GET_ENDPOINT
import com.lamblin.it.model.SINGLE_PATH_PARAM_GET_ENDPOINT
import com.lamblin.it.model.getServerBaseUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object GetControllerClient {

    private val client: GetControllerApi

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(createObjectMapper()))
            .baseUrl(getServerBaseUrl())
            .build()

        this.client = retrofit.create(GetControllerApi::class.java)
    }

    fun callSimpleGetNoParamsEndpoint() = client.callSimpleGetNoParamsEndpoint().execute()

    fun callSingleQueryParamEndpoint(queryParam: String) = client.callQueryParamEndpoint(queryParam, null).execute()

    fun callMultiQueryParamEndpoint(queryParam1: String, queryParam2: String) =
        client.callQueryParamEndpoint(queryParam1, queryParam2).execute()

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
        fun callQueryParamEndpoint(
            @Query(QUERY_PARAM_1) queryParam1: String,
            @Query(QUERY_PARAM_2) queryParam2: String?
        ): Call<ResponseEntity>

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