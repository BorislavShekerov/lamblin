package com.lamblin.local.it

import com.fasterxml.jackson.databind.ObjectMapper
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET

interface ClientApi {

    companion object {
        fun create(baseUrl: String, objectMapper: ObjectMapper): ClientApi {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                    .baseUrl(baseUrl)
                    .build()

            return retrofit.create(ClientApi::class.java)
        }
    }

    @GET("/test")
    fun test(): Call<FooResponse>
}
