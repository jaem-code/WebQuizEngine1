package engine.controller

import engine.dto.AnswerDTO
import engine.dto.NewQuizDTO
import engine.entity.Quiz
import engine.dto.ResponseDTO
import engine.service.QuizService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/quizzes")
class QuizController(private val quizService: QuizService) {

    // 새 퀴즈 생성
    @PostMapping
    fun createQuiz(
        @RequestBody newQuiz: NewQuizDTO,
        @AuthenticationPrincipal userForCreate: UserDetails
    ): ResponseEntity<Quiz> {
        val quiz = quizService.createQuiz(newQuiz, userForCreate.username)
        return ResponseEntity.ok(quiz) // 생성된 퀴즈와 함께 OK(200) 응답 반환
    }

    // 퀴즈 풀기
    @PostMapping("/{id}/solve")
    fun solveQuiz(@PathVariable id: Int, @RequestBody answer: AnswerDTO): ResponseEntity<ResponseDTO> {
        val response = quizService.solveQuiz(id, answer)
        return ResponseEntity.ok(response) // 퀴즈 풀이 결과와 함께 OK(200) 응답 반환
    }

    // ID로 특정 퀴즈 조회
    @GetMapping("/{id}")
    fun getQuiz(@PathVariable id: Int): ResponseEntity<Quiz?> {
        val quiz = quizService.getQuiz(id)
        return if (quiz != null) {
            ResponseEntity.ok(quiz) // 조회된 퀴즈와 함께 OK(200) 응답 반환
        } else {
            ResponseEntity.notFound().build() // 퀴즈가 없는 경우 NOT FOUND(404) 응답 반환
        }
    }

    // 모든 퀴즈 조회
    @GetMapping
    fun getAllQuizzes(): ResponseEntity<List<Quiz>> {
        val quizzes = quizService.getAllQuizzes()
        return ResponseEntity.ok(quizzes) // 조회된 모든 퀴즈와 함께 OK(200) 응답 반환
    }

    // 퀴즈 삭제
    @DeleteMapping("/{id}")
    fun deleteQuiz(
        @PathVariable id: Int,
        @AuthenticationPrincipal userForDetail: UserDetails
    ): ResponseEntity<Unit> {
        val result = quizService.deleteQuiz(id.toLong(), userForDetail.username)
        val statusCode = when (result) {
            QuizService.QuizDeleteResult.SUCCESS -> HttpStatus.NO_CONTENT // 퀴즈 삭제 성공 시 NO CONTENT(204) 응답 반환
            QuizService.QuizDeleteResult.NOT_FOUND -> HttpStatus.NOT_FOUND // 퀴즈가 없는 경우 NOT FOUND(404) 응답 반환
            QuizService.QuizDeleteResult.UNAUTHORIZED -> HttpStatus.FORBIDDEN // 권한이 없는 경우 FORBIDDEN(403) 응답 반환
        }
        return ResponseEntity.status(statusCode).build()
    }
}
