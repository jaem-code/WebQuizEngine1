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
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/quizzes")
class QuizController(private val quizService: QuizService) {

    @PostMapping
    fun createQuiz(
        @RequestBody newQuiz: NewQuizDTO,
        @AuthenticationPrincipal context: UserDetails
    ): ResponseEntity<Quiz> {
        val quiz = quizService.createQuiz(newQuiz, context.username)
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

    @DeleteMapping("/{id}")
    fun deleteQuiz(
        @PathVariable id: Int,
        @AuthenticationPrincipal userDetails: UserDetails // 현재 인증된 사용자 정보를 가져옵니다.
    ): ResponseEntity<Void> {
        val result = quizService.deleteQuiz(id, userDetails.username) // userDetails.username에는 이메일 주소가 있어야 합니다.
        return when(result) {
            QuizService.QuizDeleteResult.SUCCESS -> ResponseEntity.noContent().build() // 204 NO CONTENT
            QuizService.QuizDeleteResult.NOT_FOUND -> ResponseEntity.notFound().build() // 404 NOT FOUND
            QuizService.QuizDeleteResult.UNAUTHORIZED -> ResponseEntity.status(HttpStatus.FORBIDDEN).build() // 403 UNAUTHORIZED
        }
    }

}