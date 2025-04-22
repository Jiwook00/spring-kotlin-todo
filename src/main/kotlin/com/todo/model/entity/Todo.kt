package com.todo.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "todos")
class Todo(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0, // JPA가 영속화 시점에 값을 설정

    @Column(length = 100, nullable = false)
    var title: String = "",

    // nullable 속성을 지정하는 방법
    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(nullable = false)
    var isCompleted: Boolean = false,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var priority: Priority = Priority.MEDIUM,
) {

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()


    /**
     * 개변수 없는 보호된 생성자를 선언합니다. JPA가 엔티티를 초기화할 때 사용
     * protected 접근 제한자를 사용하는 이유는 이 생성자가 JPA 내부적으로만 사용되어야 하고, 애플리케이션 코드에서는 사용되지 않도록 하기 위함
     */
    protected constructor() : this(
        description = ""
    )
}


enum class Priority {
    HIGH, MEDIUM, LOW
}
