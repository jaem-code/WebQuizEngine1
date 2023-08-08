package engine.controller

import engine.dto.AnswerDTO
import engine.dto.NewQuizDTO
import engine.entity.Quiz
import engine.dto.ResponseDTO
import engine.service.QuizService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/quizzes")
class QuizController(private val quizService: QuizService) {

    @PostMapping
    fun createQuiz(
        @RequestBody newQuiz: NewQuizDTO): ResponseEntity<Quiz> {
        val quiz = quizService.createQuiz(newQuiz)
        return ResponseEntity.ok(quiz)
    }

    @PostMapping("/{id}/solve")
    fun solveQuiz(@PathVariable id: Int, @RequestBody answer: AnswerDTO): ResponseEntity<ResponseDTO> {
        val response = quizService.solveQuiz(id, answer)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{id}")
    fun getQuiz(@PathVariable id: Int): ResponseEntity<Quiz?> {
        val quiz = quizService.getQuiz(id)
        return if (quiz != null) {
            ResponseEntity.ok(quiz)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAllQuizzes(): ResponseEntity<List<Quiz>> {
        val quizzes = quizService.getAllQuizzes()
        return ResponseEntity.ok(quizzes)
    }
}