/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.exception

/** Thrown when event trigger cannot be deserialized. */
class EventDeserializationException(message: String, e: Exception) : RuntimeException(message, e)
