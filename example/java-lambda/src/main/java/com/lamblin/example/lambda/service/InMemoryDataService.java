package com.lamblin.example.lambda.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import com.lamblin.example.lambda.model.TodoList;

public class InMemoryDataService implements DataService {

  private static TodoList todoList1 = new TodoList(
      UUID.randomUUID(),
      "Test TODO 1",
      Stream.of(
          new TodoList.TodoListEntry("Foo", "Godard cray pour-over, skateboard organic viral prism"),
          new TodoList.TodoListEntry("Bar", " Activated charcoal next level trust fund master cleanse cronut"))
          .collect(toList()));

  private static TodoList todoList2 = new TodoList(
      UUID.randomUUID(),
      "Test TODO 2",
      Stream.of(
          new TodoList.TodoListEntry("Baz", "Swag squid plaid, taiyaki poke ennui occupy jianbing chia"),
          new TodoList.TodoListEntry("Bam", "Ethical butcher affogato stumptown chambray try-hard"))
          .collect(toList()));

  public List<TodoList> fetchAll() {
    return null;
  }

  public TodoList fetchList(String uid) {
    return null;
  }

  public TodoList fetchListForName(String name) {
    return null;
  }

  public void deleteList(String uid) {

  }
}
