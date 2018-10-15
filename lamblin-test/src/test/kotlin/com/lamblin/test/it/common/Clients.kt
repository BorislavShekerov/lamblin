package com.lamblin.test.it.common

import com.fasterxml.jackson.databind.ObjectMapper
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET

interface Controller1Client {

    companion object {
        fun create(baseUrl: String, objectMapper: ObjectMapper): Controller1Client {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .baseUrl(baseUrl)
                .build()

            return retrofit.create(Controller1Client::class.java)
        }
    }

    @GET("/controller1-endpoint")
    fun callEndpoint(): Call<TestResponse>
}

interface Controller2Client {

    companion object {
        fun create(baseUrl: String, objectMapper: ObjectMapper): Controller2Client {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .baseUrl(baseUrl)
                .build()

            return retrofit.create(Controller2Client::class.java)
        }
    }

    @GET("/controller2-endpoint")
    fun callEndpoint(): Call<TestResponse>
}
