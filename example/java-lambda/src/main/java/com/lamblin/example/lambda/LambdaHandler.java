package com.lamblin.example.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.google.common.collect.ImmutableSet;
import com.lamblin.core.Lamblin;
import com.lamblin.example.lambda.controller.TodoController;
import com.lamblin.example.lambda.service.DataService;
import com.lamblin.example.lambda.service.InMemoryDataService;

import java.io.InputStream;
import java.io.OutputStream;

public class LambdaHandler implements RequestStreamHandler {

  private static final DataService DATA_SERVICE = new InMemoryDataService();
  private static final TodoController TODO_CONTROLLER = new TodoController(DATA_SERVICE);

  public void handleRequest(InputStream input, OutputStream output, Context context) {
    Lamblin lamblin = Lamblin.instance(ImmutableSet.of(TODO_CONTROLLER));

    lamblin.handlerRequest(input, output);
  }
}
