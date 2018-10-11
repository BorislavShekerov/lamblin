/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.example.lambda.service;

import com.google.common.collect.ImmutableList;
import com.lamblin.example.lambda.model.TodoList;

import java.util.List;
import java.util.UUID;

public class InMemoryDataService implements DataService {

  private static TodoList todoList1 = new TodoList(
      UUID.randomUUID(),
      "Test TODO 1",
      ImmutableList.of(
          new TodoList.TodoListEntry("Foo", "Godard cray pour-over, skateboard organic viral prism"),
          new TodoList.TodoListEntry("Bar", " Activated charcoal next level trust fund master cleanse cronut")));

  private static TodoList todoList2 = new TodoList(
      UUID.randomUUID(),
      "Test TODO 2",
      ImmutableList.of(
          new TodoList.TodoListEntry("Baz", "Swag squid plaid, taiyaki poke ennui occupy jianbing chia"),
          new TodoList.TodoListEntry("Bam", "Ethical butcher affogato stumptown chambray try-hard")));

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
