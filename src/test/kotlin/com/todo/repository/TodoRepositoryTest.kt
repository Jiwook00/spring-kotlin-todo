package com.todo.repository

import com.todo.model.entity.Todo
import jakarta.persistence.EntityManager
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test

@SpringBootTest
@Transactional
class TodoRepositoryTest {

    @Autowired
    private lateinit var todoRepository: TodoRepository

    @Autowired
    private lateinit var em: EntityManager

    @Test
    fun `새로운 Todo 저장 시 ID가 생성되어야 한다`() {
        // given
        val todo = Todo(title = "테스트 할 일")

        // when
        val savedTodo = todoRepository.save(todo)

        // then
        assertThat(savedTodo.id).isNotEqualTo(0L)
    }

    @Test
    fun `ID로 Todo 찾기 성공 시 올바른 Todo를 반환해야 한다`() {
        // given
        val todo = Todo(
            title = "테스트 할일",
            description = "테스트 설명"
        )
        val savedTodo = todoRepository.save(todo)

        // when
        val findTodo = todoRepository.findOne(savedTodo.id)

        println("@@@@@@@@@ Todo: ${todo.title} @@@@@ findTodo: ${findTodo?.title} ")

        // then
        assertThat(findTodo).isNotNull
        Assertions.assertEquals(todo.title, findTodo?.title)
        Assertions.assertEquals(todo.description, findTodo?.description)
        Assertions.assertEquals(todo.isCompleted, false)
    }

    @Test
    fun `모든 Todo 조회 시 저장된 모든 Todo를 반환한다`() {
        // given
        val todo1 = Todo(title = "할일 1", description = "설명 1")
        val todo2 = Todo(title = "할일 2", description = "설명 2")
        val todo3 = Todo(title = "할일 3", description = "설명 3")

        todoRepository.save(todo1)
        todoRepository.save(todo2)
        todoRepository.save(todo3)

        // when
        val todoList = todoRepository.findAll()

        // then
        assertThat(todoList).hasSize(3)
        assertThat(todoList.map { it.title }).containsExactlyInAnyOrder("할일 1", "할일 2", "할일 3")
    }

    @Test
    fun `완료 상태로 Todo 필터링 시 해당 상태의 Todo만 반환되어야 한다`() {
        // given
        val todo1 = Todo(title = "완료할 할일 1")
        val todo2 = Todo(title = "할일 2")
        val todo3 = Todo(title = "할일 3")

        val savedTodo1 = todoRepository.save(todo1)
        todoRepository.save(todo2)
        todoRepository.save(todo3)

        todoRepository.updateCompletionStatus(savedTodo1.id, true)

        // when
        val completedTodoList = todoRepository.findByIsCompleted(true)
        val incompletedTodoList = todoRepository.findByIsCompleted(false)

        // then
        assertThat(completedTodoList).hasSize(1)
        assertThat(completedTodoList.map { it.title }).containsExactlyInAnyOrder("완료할 할일 1")

        assertThat(incompletedTodoList).hasSize(2)
        assertThat(incompletedTodoList.map { it.title }).containsExactlyInAnyOrder("할일 2", "할일 3")
    }

    @Test
    fun `키워드로 Todo 검색 시 제목이나 설명에 키워드가 포함된 Todo만 반환해야 한다`() {
        // given
        val todo1Title = "JPA 강의"
        val todo2Title = "Todo 앱 만들기"
        todoRepository.save(Todo(title = todo1Title, description = "JPA 기본 강의 학습하기"))
        todoRepository.save(Todo(title = todo2Title, description = "JPA와 스프링을 활용하기"))

        // when
        val result1 = todoRepository.findByTitleOrDescription("JPA")
        val result2 = todoRepository.findByTitleOrDescription("Todo")

        // then
        // JPA 검색 결과 확인
        assertThat(result1).hasSize(2)
        assertThat(result1.map { it.title }).containsExactlyInAnyOrder(todo1Title, todo2Title)

        // Todo 검색 결과 확인
        assertThat(result2).hasSize(1)
        assertThat(result2[0].title).isEqualTo(todo2Title)

    }

    @Test
    fun `Todo 삭제 시 해당 Todo가 제거되어야 한다`() {
        // given
        val todo = Todo(title = "삭제할 할일")
        val savedTodo = todoRepository.save(todo)
        val savedId = savedTodo.id

        // when
        todoRepository.delete(savedId)

        // then
        assertThat(todoRepository.findOne(savedId)).isNull()
    }

    @Test
    fun `Todo 수정 시 title이 정상적으로 변경되어야 한다`() {
        // given
        val originalTodo = Todo(title = "원래 제목", description = "원래 설명")
        val savedTodo = todoRepository.save(originalTodo)
        val savedId = savedTodo.id

        // when
        val updateTodo = Todo(
            id = savedId,
            title = "수정된 제목",
            description = "원래 설명",
            isCompleted = false,
        )
        todoRepository.save(updateTodo)

        // then
        val findTodo = todoRepository.findOne(savedId)
        assertThat(findTodo).isNotNull
        assertThat(findTodo?.title).isEqualTo("수정된 제목")
        assertThat(findTodo?.description).isEqualTo("원래 설명")
    }

}