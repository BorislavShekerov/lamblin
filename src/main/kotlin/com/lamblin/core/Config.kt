package com.lamblin.core

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule

val OBJECT_MAPPER = ObjectMapper().apply { registerModule(JavaTimeModule()) }
