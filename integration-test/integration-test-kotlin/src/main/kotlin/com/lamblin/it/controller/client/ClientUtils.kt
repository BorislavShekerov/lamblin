/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.it.controller.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

fun createObjectMapper(): ObjectMapper {
    val objectMapper = ObjectMapper()
    objectMapper.registerModule(KotlinModule())

    return objectMapper
}