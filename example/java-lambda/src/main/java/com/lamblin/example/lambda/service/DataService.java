package com.lamblin.example.lambda.service;

import com.lamblin.example.lambda.model.TodoList;

import java.util.List;

public interface DataService {

    List<TodoList> fetchAll();

    TodoList fetchList(String uid);

    TodoList fetchListForName(String name);

    void deleteList(String uid);
    
}
