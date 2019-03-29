/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

/** Defines and configures an object mapper to be shared. */
internal val OBJECT_MAPPER = ObjectMapper().apply {
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    registerModule(JavaTimeModule())
    registerModule(KotlinModule())
}

internal val jsonMapperRegistry = JsonMapperRegistry()

/** Allows for a custom ObjectMapper to be provided. */
class JsonMapperRegistry(internal var jsonMapper: ObjectMapper = OBJECT_MAPPER) {

    /** Specifies the object mapper to be used. */
    fun registerObjectMapper(objectMapperToUse: ObjectMapper) {
        this.jsonMapper = objectMapperToUse
    }
}

/** Injects the custom object mapper to be used accross Lamblin. */
fun Lamblin.withObjectMapper(objectMapper: ObjectMapper): Lamblin {
    jsonMapperRegistry.registerObjectMapper(objectMapper)

    return this
}