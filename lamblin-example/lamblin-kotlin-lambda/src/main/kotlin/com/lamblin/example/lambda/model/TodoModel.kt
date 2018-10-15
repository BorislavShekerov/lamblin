/*
 * Lamblin
 * Copyright 2018 Borislav Shekerov
 * Licensed under Apache 2.0: https://github.com/BorislavShekerov/lamblin/blob/master/LICENSE
 */

package com.lamblin.example.lambda.model

import java.util.UUID

data class TodoList(val uid: UUID, val name: String, val entries: List<TodoListEntry>)

data class TodoListEntry(val name: String, val description: String)
