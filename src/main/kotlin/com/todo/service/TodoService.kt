package com.todo.service

import com.todo.model.entity.Priority
import com.todo.model.entity.Todo

interface TodoService {
    fun createTodo(title: String, description: String?, priority: Priority = Priority.MEDIUM): Todo
    fun getTodoById(id: Long): Todo?
    fun getAllTodoList(): List<Todo>
    fun getCompletedTodoList(): List<Todo>
    fun getIncompleteTodoList(): List<Todo>
    fun updateTodo(id: Long, title: String, description: String?, priority: Priority): Todo
    fun toggleTodoStatus(id: Long): Todo
    fun deleteTodo(id: Long)
    fun searchTodoList(keyword: String): List<Todo>
}