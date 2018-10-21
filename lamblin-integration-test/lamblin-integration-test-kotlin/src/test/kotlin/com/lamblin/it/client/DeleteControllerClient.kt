/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.it.client

import com.lamblin.it.model.MULTIPLE_PATH_PARAM_DELETE_ENDPOINT
import com.lamblin.it.model.PATH_PARAM_1
import com.lamblin.it.model.PATH_PARAM_2
import com.lamblin.it.model.QUERY_PARAM_1
import com.lamblin.it.model.QUERY_PARAM_2
import com.lamblin.it.model.QUERY_PARAM_DELETE_ENDPOINT
import com.lamblin.it.model.ResponseEntity
import com.lamblin.it.model.SIMPLE_DELETE_ENDPOINT
import com.lamblin.it.model.SINGLE_PATH_PARAM_DELETE_ENDPOINT
import com.lamblin.it.model.getServerBaseUrl
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

object DeleteControllerClient {

    private val client: DeleteControllerApi

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(createObjectMapper()))
            .client(
                OkHttpClient.Builder()
                    .readTimeout(1, TimeUnit.HOURS)
                    .build())
            .baseUrl(getServerBaseUrl())
            .build()
        client = retrofit.create(DeleteControllerApi::class.java)
    }

    fun callSimpleDeleteNoParamsEndpoint() = client.callSimpleDeleteNoParamsEndpoint().execute()

    fun callSingleQueryParamEndpoint(queryParam: String) = client.callSingleQueryParamEndpoint(queryParam).execute()

    fun callMultiQueryParamEndpoint(queryParam1: String, queryParam2: String) = client.callQueryParamEndpoint(
        queryParam1, queryParam2
    ).execute()

    fun callSinglePathParamEndpoint(pathParam: String) = client.callSinglePathParamEndpoint(pathParam).execute()

    fun callMultiPathParamEndpoint(pathParam1: String, pathParam2: String) = client.callMultiPathParamEndpoint(
        pathParam1, pathParam2
    ).execute()

    fun callMultiPathParamWithQueryParamEndpoint(
        queryParam: String,
        pathParam1: String,
        pathParam2: String
    ) = client.callMultiPathParamEndpoint(pathParam1, pathParam2, queryParam).execute()

    internal interface DeleteControllerApi {

        @DELETE(SIMPLE_DELETE_ENDPOINT)
        fun callSimpleDeleteNoParamsEndpoint(): Call<ResponseEntity>

        @DELETE(QUERY_PARAM_DELETE_ENDPOINT)
        fun callSingleQueryParamEndpoint(@Query(QUERY_PARAM_1) queryParam: String): Call<ResponseEntity>

        @DELETE(QUERY_PARAM_DELETE_ENDPOINT)
        fun callQueryParamEndpoint(
            @Query(QUERY_PARAM_1) queryParam1: String,
            @Query(QUERY_PARAM_2) queryParam2: String
        ): Call<ResponseEntity>

        @DELETE(SINGLE_PATH_PARAM_DELETE_ENDPOINT)
        fun callSinglePathParamEndpoint(@Path(PATH_PARAM_1) pathParam1: String): Call<ResponseEntity>

        @DELETE(MULTIPLE_PATH_PARAM_DELETE_ENDPOINT)
        fun callMultiPathParamEndpoint(
            @Path(PATH_PARAM_1) pathParam1: String,
            @Path(PATH_PARAM_2) pathParam2: String
        ): Call<ResponseEntity>

        @DELETE(MULTIPLE_PATH_PARAM_DELETE_ENDPOINT)
        fun callMultiPathParamEndpoint(
            @Path(PATH_PARAM_1) pathParam1: String,
            @Path(PATH_PARAM_2) pathParam2: String,
            @Query(QUERY_PARAM_1) queryParam1: String
        ): Call<ResponseEntity>
    }
}