package com.todo.controller

import com.todo.model.entity.Priority
import com.todo.model.entity.Todo
import com.todo.service.TodoService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/todos")
class TodoController(private val todoService: TodoService) {

    @GetMapping
    fun listTodos(@RequestParam(required = false) filter: String?,
                  @RequestParam(required = false) search: String?,
                  model: Model): String {
        val todoList = when(filter) {
            "completed" -> todoService.getCompletedTodoList()
            "incomplete" -> todoService.getIncompleteTodoList()
            else -> if (search != null) todoService.searchTodoList(search) else todoService.getAllTodoList()
        }

        model.addAttribute("todos", todoList)
        model.addAttribute("filter", filter ?: "all")
        model.addAttribute("search", search ?: "")
        model.addAttribute("priorities", Priority.values())
        model.addAttribute("newTodo", Todo())

        return "todos"
    }

    @PostMapping
    fun createTodo(
        @RequestParam title: String,
        @RequestParam(required = false) description: String?,
        @RequestParam priority: Priority,
        redirectAttributes: RedirectAttributes
    ): String {
        val todo = todoService.createTodo(title, description, priority)
        redirectAttributes.addFlashAttribute("message", "할 일이 성공적으로 추가되었습니다.")

        return "redirect:/todos"
    }

    // 수정 필요
    @PostMapping("/{id}")
    fun updateTodo(@PathVariable id: Long,
                   @RequestParam title: String,
                   @RequestParam(required = false) description: String?,
                   @RequestParam priority: Priority,
                   redirectAttributes: RedirectAttributes): String {
        todoService.updateTodo(id, title, description, priority)
        redirectAttributes.addFlashAttribute("message", "할 일이 성공적으로 수정되었습니다.")

        return "redirect:/todos"
    }

    @PostMapping("/{id}/toggle")
    fun toggleTodoStatus(@PathVariable id: Long, redirectAttributes: RedirectAttributes): String {
        val todo = todoService.toggleTodoStatus(id)
        val status = if (todo.isCompleted) "완료" else "미완료"
        redirectAttributes.addFlashAttribute("message", "할 일 상태가 ${status}로 변경되었습니다.")

        return "redirect:/todos"
    }

    @PostMapping("/{id}/delete")
    fun deleteTodo(@PathVariable id: Long, redirectAttributes: RedirectAttributes): String {
        todoService.deleteTodo(id)
        redirectAttributes.addFlashAttribute("message", "할 일이 성공적으로 삭제되었습니다.")

        return "redirect:/todos"
    }

    @GetMapping("/search")
    fun searchTodos(@RequestParam keyword: String, model: Model): String {
        return "redirect:/todos?search=${keyword}"
    }
}