/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.core.exception

import java.lang.Exception
import java.lang.RuntimeException

/** Thrown when request payload of a POST/PUT/PATCH request could not be deserialized in the target entity. */
class RequestPayloadParseException(message: String, e: Exception) : RuntimeException(message, e)
