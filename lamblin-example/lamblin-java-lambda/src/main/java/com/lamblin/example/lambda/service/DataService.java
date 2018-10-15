/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.example.lambda.service;

import com.lamblin.example.lambda.model.TodoList;

import java.util.List;

public interface DataService {

    List<TodoList> fetchAll();

    TodoList fetchList(String uid);

    TodoList fetchListForName(String name);

    void deleteList(String uid);
    
}
