package com.lamblin.it.controller.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule

fun createObjectMapper(): ObjectMapper {
    val objectMapper = ObjectMapper()
    objectMapper.registerModule(KotlinModule())

    return objectMapper
}