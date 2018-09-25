package com.lamblin.core.exception

import java.lang.Exception
import java.lang.RuntimeException

class RequestPayloadParseException(message: String, e: Exception): RuntimeException(message, e)
