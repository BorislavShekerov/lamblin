package com.lamblin.example.lambda.controller;

import static com.lamblin.core.model.HttpResponse.ok;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.lamblin.core.model.HttpMethod;
import com.lamblin.core.model.HttpResponse;
import com.lamblin.core.model.annotation.Controller;
import com.lamblin.core.model.annotation.Endpoint;
import com.lamblin.core.model.annotation.PathParam;
import com.lamblin.core.model.annotation.QueryParam;
import com.lamblin.core.model.annotation.RequestBody;
import com.lamblin.example.lambda.model.TodoList;
import com.lamblin.example.lambda.service.DataService;

@Controller
public class TodoController {

  private static final String TODOS_ROOT_ENDPOINT = "/todo-lists";
  private static final String TODOS_SPECIFIC_LIST = "/todo-lists/{uid}";

  private final DataService dataService;

  public TodoController(DataService dataService) {
    this.dataService = dataService;
  }

  @Endpoint(path = TODOS_ROOT_ENDPOINT, method = HttpMethod.GET)
  public HttpResponse<List<TodoList>> getAllTodoLists() {
    return ok(dataService.fetchAll());
  }

  @Endpoint(path = TODOS_ROOT_ENDPOINT, method = HttpMethod.POST)
  public HttpResponse<List<TodoList>> createTodoList(@RequestBody TodoList todoList) {
    return ok(ImmutableList.<TodoList>builder()
        .addAll(dataService.fetchAll())
        .add(todoList)
        .build());
  }

  @Endpoint(path = TODOS_ROOT_ENDPOINT, method = HttpMethod.GET)
  public HttpResponse<TodoList> getTodoListByName(@QueryParam("name") String name) {
    return ok(dataService.fetchListForName(name));
  }

  @Endpoint(path = TODOS_SPECIFIC_LIST, method = HttpMethod.GET)
  public HttpResponse<TodoList> getSpecificTodoList(@PathParam("uid") String uid) {
    return ok(dataService.fetchList(uid));
  }

  @Endpoint(path = TODOS_SPECIFIC_LIST, method = HttpMethod.DELETE)
  public HttpResponse<Void> deleteTodoList(@PathParam("uid") String uid) {
    dataService.deleteList(uid);

    return ok();
  }

}
