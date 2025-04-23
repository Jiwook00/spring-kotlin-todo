package com.todo.service

import com.todo.model.entity.Priority
import com.todo.model.entity.Todo
import com.todo.repository.TodoRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class TodoServiceImp(
    private val todoRepository: TodoRepository
): TodoService {

    @Transactional
    override fun createTodo(title: String, description: String?, priority: Priority): Todo {
        val todo = Todo(
            title = title,
            description = description,
            priority = priority
        )
        return todoRepository.save(todo)
    }

    override fun getTodoById(id: Long): Todo? {
        return todoRepository.findOne(id)
    }

    override fun getAllTodoList(): List<Todo> {
        return todoRepository.findAll()
    }

    override fun getCompletedTodoList(): List<Todo> {
        return todoRepository.findByIsCompleted(true)
    }

    override fun getIncompleteTodoList(): List<Todo> {
        return todoRepository.findByIsCompleted(false)
    }

    @Transactional
    override fun updateTodo(id: Long, title: String, description: String?, priority: Priority): Todo {
        val todo = todoRepository.findOne(id) ?: throw IllegalArgumentException("Todo 아이디 $id 를 찾을 수 없습니다.")
        todo.title = title
        todo.description = description
        todo.priority = priority
        todo.updatedAt = LocalDateTime.now()
        todoRepository.save(todo)

        return todo
    }

    @Transactional
    override fun toggleTodoStatus(id: Long): Todo {
        val todo = todoRepository.findOne(id) ?: throw IllegalArgumentException("Todo 아이디 $id 를 찾을 수 없습니다.")
        return todoRepository.updateCompletionStatus(id, !todo.isCompleted)
    }

    @Transactional
    override fun deleteTodo(id: Long) {
        todoRepository.delete(id)
    }

    override fun searchTodoList(keyword: String): List<Todo> {
        return todoRepository.findByTitleOrDescription(keyword)
    }

}