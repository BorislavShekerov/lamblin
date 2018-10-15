/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.it.client

import com.lamblin.it.model.ExampleRequestBody
import com.lamblin.it.model.MULTI_PATH_PARAM_PUT_ENDPOINT
import com.lamblin.it.model.PATH_PARAM_1
import com.lamblin.it.model.PATH_PARAM_2
import com.lamblin.it.model.QUERY_PARAM_1
import com.lamblin.it.model.QUERY_PARAM_2
import com.lamblin.it.model.QUERY_PARAM_PUT_ENDPOINT
import com.lamblin.it.model.ResponseEntity
import com.lamblin.it.model.SIMPLE_PUT_ENDPOINT
import com.lamblin.it.model.SIMPLE_REQUEST_BODY_PUT_ENDPOINT
import com.lamblin.it.model.SINGLE_PATH_PARAM_PUT_ENDPOINT
import com.lamblin.it.model.getServerBaseUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

object PutControllerClient {

    private val client: PutControllerApi

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(createObjectMapper()))
            .baseUrl(getServerBaseUrl())
            .build()

        client = retrofit.create(PutControllerApi::class.java)
    }

    fun callSimplePutNoParamsEndpoint() = client.callSimplePostNoParamsEndpoint().execute()

    fun callSingleQueryParamEndpoint(queryParam: String) = client.callSingleQueryParamEndpoint(queryParam).execute()

    fun callMultiQueryParamEndpoint(queryParam1: String, queryParam2: String) =
        client.callMultiQueryParamEndpoint(queryParam1, queryParam2).execute()

    fun callSinglePathParamEndpoint(pathParam: String) = client.callSinglePathParamEndpoint(pathParam).execute()

    fun callMultiPathParamEndpoint(pathParam1: String, pathParam2: String) =
        client.callMultiPathParamEndpoint(pathParam1, pathParam2).execute()

    fun callMultiPathParamEndpointWithQueryParamEndpoint(
        queryParam: String,
        pathParam1: String,
        pathParam2: String
    ) = client.callMultiPathParamEndpoint(pathParam1, pathParam2, queryParam).execute()

    fun callRequestBodyEndpoint(exampleRequestBod: ExampleRequestBody) =
        client.callRequestBodyEndpoint(exampleRequestBod).execute()

    private interface PutControllerApi {

        @PUT(SIMPLE_PUT_ENDPOINT)
        fun callSimplePostNoParamsEndpoint(): Call<ResponseEntity>

        @PUT(QUERY_PARAM_PUT_ENDPOINT)
        fun callSingleQueryParamEndpoint(@Query(QUERY_PARAM_1) queryParam: String): Call<ResponseEntity>

        @PUT(QUERY_PARAM_PUT_ENDPOINT)
        fun callMultiQueryParamEndpoint(
            @Query(QUERY_PARAM_1) queryParam1: String,
            @Query(QUERY_PARAM_2) queryParam2: String
        ): Call<ResponseEntity>

        @PUT(SINGLE_PATH_PARAM_PUT_ENDPOINT)
        fun callSinglePathParamEndpoint(@Path(PATH_PARAM_1) pathParam1: String): Call<ResponseEntity>

        @PUT(MULTI_PATH_PARAM_PUT_ENDPOINT)
        fun callMultiPathParamEndpoint(
            @Path(PATH_PARAM_1) pathParam1: String,
            @Path(PATH_PARAM_2) pathParam2: String
        ): Call<ResponseEntity>

        @PUT(MULTI_PATH_PARAM_PUT_ENDPOINT)
        fun callMultiPathParamEndpoint(
            @Path(PATH_PARAM_1) pathParam1: String,
            @Path(PATH_PARAM_2) pathParam2: String,
            @Query(QUERY_PARAM_1) queryParam1: String
        ): Call<ResponseEntity>

        @PUT(SIMPLE_REQUEST_BODY_PUT_ENDPOINT)
        fun callRequestBodyEndpoint(@Body exampleRequestBody: ExampleRequestBody): Call<ResponseEntity>

    }
}