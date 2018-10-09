package com.lamblin.it.controller.client

import com.lamblin.it.model.ExampleRequestBody
import com.lamblin.it.model.MULTI_PATH_PARAM_PATCH_ENDPOINT
import com.lamblin.it.model.PATH_PARAM_1
import com.lamblin.it.model.PATH_PARAM_2
import com.lamblin.it.model.QUERY_PARAM_1
import com.lamblin.it.model.QUERY_PARAM_2
import com.lamblin.it.model.QUERY_PARAM_PATCH_ENDPOINT
import com.lamblin.it.model.ResponseEntity
import com.lamblin.it.model.SIMPLE_PATCH_ENDPOINT
import com.lamblin.it.model.SIMPLE_REQUEST_BODY_PATCH_ENDPOINT
import com.lamblin.it.model.SINGLE_PATH_PARAM_PATCH_ENDPOINT
import com.lamblin.it.model.getServerBaseUrl
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

object PatchControllerClient {

    private val client: PatchControllerApi

    init {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(JacksonConverterFactory.create(createObjectMapper()))
            .baseUrl(getServerBaseUrl())
            .build()

        this.client = retrofit.create(PatchControllerApi::class.java)
    }

    fun callSimplePatchNoParamsEndpoint() = client.callSimplePatchNoParamsEndpoint().execute()

    fun callSingleQueryParamEndpoint(queryParam: String) = client.callSingleQueryParamEndpoint(queryParam).execute()

    fun callMultiQueryParamEndpoint(queryParam1: String, queryParam2: String) = client.callMultiQueryParamEndpoint(
        queryParam1, queryParam2
    ).execute()

    fun callSinglePathParamEndpoint(pathParam: String) = client.callSinglePathParamEndpoint(pathParam).execute()

    fun callMultiPathParamEndpoint(pathParam1: String, pathParam2: String) = client.callMultiPathParamEndpoint(
        pathParam1, pathParam2
    ).execute()

    fun callMultiPathParamEndpointWithQueryParam(
        queryParam: String,
        pathParam1: String,
        pathParam2: String
    ) = client.callMultiPathParamEndpoint(pathParam1, pathParam2, queryParam).execute()

    fun callRequestBodyEndpoint(exampleRequestBod: ExampleRequestBody) = client.callRequestBodyEndpoint(
        exampleRequestBod
    ).execute()

    private interface PatchControllerApi {

        @PATCH(SIMPLE_PATCH_ENDPOINT)
        fun callSimplePatchNoParamsEndpoint(): Call<ResponseEntity>

        @PATCH(QUERY_PARAM_PATCH_ENDPOINT)
        fun callSingleQueryParamEndpoint(@Query(QUERY_PARAM_1) queryParam: String): Call<ResponseEntity>

        @PATCH(QUERY_PARAM_PATCH_ENDPOINT)
        fun callMultiQueryParamEndpoint(
            @Query(QUERY_PARAM_1) queryParam1: String,
            @Query(QUERY_PARAM_2) queryParam2: String
        ): Call<ResponseEntity>

        @PATCH(SINGLE_PATH_PARAM_PATCH_ENDPOINT)
        fun callSinglePathParamEndpoint(@Path(PATH_PARAM_1) pathParam1: String): Call<ResponseEntity>

        @PATCH(MULTI_PATH_PARAM_PATCH_ENDPOINT)
        fun callMultiPathParamEndpoint(
            @Path(PATH_PARAM_1) pathParam1: String,
            @Path(PATH_PARAM_2) pathParam2: String
        ): Call<ResponseEntity>

        @PATCH(MULTI_PATH_PARAM_PATCH_ENDPOINT)
        fun callMultiPathParamEndpoint(
            @Path(PATH_PARAM_1) pathParam1: String,
            @Path(PATH_PARAM_2) pathParam2: String,
            @Query(QUERY_PARAM_1) queryParam1: String
        ): Call<ResponseEntity>

        @PATCH(SIMPLE_REQUEST_BODY_PATCH_ENDPOINT)
        fun callRequestBodyEndpoint(@Body exampleRequestBody: ExampleRequestBody): Call<ResponseEntity>

    }
}