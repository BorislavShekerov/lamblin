package com.lamblin.example.lambda.controller

import com.lamblin.core.model.HttpMethod
import com.lamblin.core.model.HttpResponse
import com.lamblin.core.model.annotation.*
import com.lamblin.example.lambda.model.TodoList
import com.lamblin.example.lambda.service.DataService

const val TODOS_ROOT_ENDPOINT = "/todo-lists"
const val TODOS_SPECIFIC_LIST = "/todo-lists/{uid}"

@Controller
class TodoController(private val dataService: DataService) {

    @Endpoint(TODOS_ROOT_ENDPOINT, HttpMethod.GET)
    fun getAllTodoLists() = HttpResponse.ok(dataService.fetchAll())

    @Endpoint(TODOS_ROOT_ENDPOINT, HttpMethod.POST)
    fun createTodoList(@RequestBody todoList: TodoList) = HttpResponse.ok(dataService.fetchAll() + todoList)

    @Endpoint(TODOS_ROOT_ENDPOINT, HttpMethod.GET)
    fun getTodoListByName(@QueryParam("name") name: String) = HttpResponse.ok(dataService.fetchListForName(name))

    @Endpoint(TODOS_SPECIFIC_LIST, HttpMethod.GET)
    fun getSpecificTodoList(@PathParam("uid") uid: String) = HttpResponse.ok(dataService.fetchList(uid))

    @Endpoint(TODOS_SPECIFIC_LIST, HttpMethod.DELETE)
    fun deleteTodoList(@PathParam("uid") uid: String): HttpResponse<Void> {
        dataService.deleteList(uid)

        return HttpResponse.ok()
    }

}