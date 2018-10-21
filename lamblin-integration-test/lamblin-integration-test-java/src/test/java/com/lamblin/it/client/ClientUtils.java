/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.it.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.function.Supplier;

class ClientUtils {

    private ClientUtils() {
    }

    static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new KotlinModule());

        return objectMapper;
    }

    static <T> Response<T> executeRequest(Supplier<Call<T>> requestCallSupplier) {
        try {
            return requestCallSupplier.get().execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
