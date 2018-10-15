/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.example.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.lamblin.core.Lamblin
import com.lamblin.example.lambda.controller.TodoController
import com.lamblin.example.lambda.service.InMemoryDataService
import java.io.InputStream
import java.io.OutputStream

val todoController = TodoController(InMemoryDataService)
val lamblin = Lamblin.frontController(todoController)

/** Defines the main Handler class used by Lambda. */
class LambdaHandler : RequestStreamHandler {

    override fun handleRequest(input: InputStream, output: OutputStream, p2: Context) {
        lamblin.handlerRequest(input, output)
    }

}