package com.lamblin.example.lambda

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.lamblin.core.Lamblin
import com.lamblin.example.lambda.controller.ExampleTodoController
import com.lamblin.example.lambda.service.InMemoryDataService
import java.io.InputStream
import java.io.OutputStream

val todoController = ExampleTodoController(InMemoryDataService)

class LambdaHandler : RequestStreamHandler {

    override fun handleRequest(input: InputStream, output: OutputStream, p2: Context) {
        val lamblin = Lamblin.instance(setOf(todoController))

        lamblin.handlerRequest(input, output)
    }

}