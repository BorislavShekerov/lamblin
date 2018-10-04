package com.lamblin.core.exception

/** Thrown when event trigger cannot be deserialized. */
class EventDeserializationException(message: String, e: Exception) : RuntimeException(message, e)
