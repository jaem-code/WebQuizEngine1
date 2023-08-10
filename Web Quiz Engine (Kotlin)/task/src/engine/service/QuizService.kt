package engine.service

import engine.dto.AnswerDTO
import engine.dto.NewQuizDTO
import engine.dto.ResponseDTO
import engine.entity.Quiz
import engine.repository.QuizRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class QuizService(
    private val quizRepository: QuizRepository
) {
    // 새 퀴즈 생성
    fun createQuiz(newQuiz: NewQuizDTO, creatorEmail: String): Quiz {
        // 입력 검증: 제목과 옵션 검사
        if (newQuiz.title.isNullOrEmpty()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required.") // 제목이 없을 경우 BAD REQUEST(400) 예외 발생
        }

        if (newQuiz.options.isNullOrEmpty()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Options cannot be empty.") // 옵션이 없을 경우 BAD REQUEST(400) 예외 발생
        }

        // 퀴즈 객체 생성
        val quiz = Quiz(
            title = newQuiz.title,
            text = newQuiz.text,
            options = newQuiz.options,
            answer = newQuiz.answer ?: listOf(), // 답변이 없을 경우 빈 리스트로 초기화
            creatorEmail = creatorEmail
        )

        // 퀴즈 저장
        val savedQuiz = quizRepository.save(quiz)

        // 저장된 퀴즈 정보 반환
        return Quiz(
            id = savedQuiz.id,
            title = savedQuiz.title,
            text = savedQuiz.text,
            options = savedQuiz.options
        )
    }

    // 퀴즈 풀이
    fun solveQuiz(id: Int, answer: AnswerDTO): ResponseDTO {
        val quiz = getQuiz(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found.") // 퀴즈가 없을 경우 NOT FOUND(404) 예외 발생

        val isCorrectAnswer = answer.answer == quiz.answer
        val responseMessage = if (isCorrectAnswer) "Congratulations, you're right!" else "Wrong answer! Please, try again."

        return ResponseDTO(isCorrectAnswer, responseMessage)
    }

    // ID로 특정 퀴즈 조회
    fun getQuiz(id: Int): Quiz? = quizRepository.findQuizById(id)

    // 모든 퀴즈 조회
    fun getAllQuizzes(): List<Quiz> =
        quizRepository.findAll()

    // 퀴즈 삭제 결과 열거형
    enum class QuizDeleteResult {
        SUCCESS, NOT_FOUND, UNAUTHORIZED
    }

    // 퀴즈 삭제
    fun deleteQuiz(id: Long, userEmail: String): QuizDeleteResult {
        val quiz = quizRepository.findById(id).orElse(null) ?: return QuizDeleteResult.NOT_FOUND // 퀴즈가 없을 경우 NOT FOUND(404) 반환

        if (quiz.creatorEmail != userEmail) return QuizDeleteResult.UNAUTHORIZED // 퀴즈 생성자의 이메일과 현재 사용자의 이메일이 다를 경우 UNAUTHORIZED(403) 반환

        quizRepository.delete(quiz)
        return QuizDeleteResult.SUCCESS // 퀴즈 삭제 성공 시 SUCCESS 반환
    }
}
