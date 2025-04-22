package com.todo.repository

import com.todo.model.entity.Todo
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class TodoRepository(
    private val em: EntityManager,
) {

    fun save(todo: Todo): Todo {
        if (todo.id == 0L) {
            em.persist(todo)
            return todo
        } else {
            // 변경 감지(dirty checking)를 사용하는 방식
            val existingTodo = em.find(Todo::class.java, todo.id)
            if (existingTodo != null) {
                existingTodo.title = todo.title
                existingTodo.description = todo.description
                existingTodo.isCompleted = todo.isCompleted
                existingTodo.updatedAt = todo.updatedAt
                return existingTodo
            } else {
                throw IllegalArgumentException("Todo 아이디 ${todo.id} 를 찾을 수 없습니다. ")
            }
        }
    }

    fun findOne(id: Long): Todo? {
        return em.find(Todo::class.java, id)
    }

    fun findAll(): List<Todo> {
        return em.createQuery("select t from Todo t", Todo::class.java).resultList
    }

    fun updateCompletionStatus(id: Long, isCompleted: Boolean): Todo {
        val todo = findOne(id) ?: throw IllegalArgumentException("Todo 아이디 $id 를 찾을 수 없습니다.")
        todo.isCompleted = isCompleted
        todo.updatedAt = LocalDateTime.now()
        return todo
    }

    fun findByIsCompleted(isCompleted: Boolean): List<Todo> {
        return em.createQuery("select t from Todo t where t.isCompleted = :isCompleted", Todo::class.java)
            .setParameter("isCompleted", isCompleted)
            .resultList
    }

    fun findByTitleOrDescription(keyword: String): List<Todo> {
        return em.createQuery(
            "select t from Todo t where t.title like :keyword or t.description like :keyword",
            Todo::class.java
        )
            .setParameter("keyword", "%$keyword%")
            .resultList
    }

    fun delete(id: Long) {
        val todo = findOne(id)
        if (todo != null) em.remove(todo)
    }
}