/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.it.model

/** The response entity use by the test controllers. */
data class ResponseEntity(val content: String)

/** Defines the request body used in POST/PUT/PATCH requests. */
data class ExampleRequestBody(val body: String)
