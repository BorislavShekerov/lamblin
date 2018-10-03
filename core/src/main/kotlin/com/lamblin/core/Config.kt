package com.lamblin.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule

/** Defines and configures an object mapper to be shared. */
internal val OBJECT_MAPPER = ObjectMapper().apply {
    registerModule(JavaTimeModule())
    registerModule(KotlinModule())
}