package com.lamblin.example.lambda.service

import com.lamblin.example.lambda.model.TodoList
import com.lamblin.example.lambda.model.TodoListEntry
import java.util.*

interface DataService {

    fun fetchAll(): List<TodoList>

    fun fetchList(uid: String): TodoList?

    fun fetchListForName(name: String): TodoList?

    fun deleteList(uid: String)
}

object InMemoryDataService : DataService {

    private val todoList1 = TodoList(
        UUID.randomUUID(),
        "Test TODO 1",
        listOf(
            TodoListEntry("Foo", "Godard cray pour-over, skateboard organic viral prism"),
            TodoListEntry("Bar", " Activated charcoal next level trust fund master cleanse cronut")))

    private val todoList2 = TodoList(
        UUID.randomUUID(),
        "Test TODO 2",
        listOf(
            TodoListEntry("Baz", "Swag squid plaid, taiyaki poke ennui occupy jianbing chia"),
            TodoListEntry("Bam", "Ethical butcher affogato stumptown chambray try-hard")))


    override fun fetchList(uid: String) =
        when (uid) {
            todoList1.uid.toString() -> todoList1
            todoList2.uid.toString() -> todoList2
            else -> null
        }


    override fun fetchListForName(name: String) =
        when (name) {
            todoList1.name -> todoList1
            todoList2.name -> todoList2
            else -> null
        }

    override fun fetchAll() = listOf(todoList1, todoList2)

    override fun deleteList(uid: String) {
    }

}