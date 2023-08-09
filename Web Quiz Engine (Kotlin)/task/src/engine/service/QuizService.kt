package engine.service

import engine.dto.AnswerDTO
import engine.dto.NewQuizDTO
import engine.dto.ResponseDTO
import engine.entity.Quiz
import engine.repository.QuizRepository
import jakarta.validation.constraints.Email
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class QuizService(
    private val quizRepository: QuizRepository
) {

    private val rightAnswerResponse: ResponseDTO =
        ResponseDTO(true, "Congratulations, you're right!")

    private val wrongAnswerResponse: ResponseDTO =
        ResponseDTO(false, "Wrong answer! Please, try again.")


    fun solveQuiz(id: Int, answer: AnswerDTO): ResponseDTO? {
        val quiz: Quiz? = getQuiz(id)

        return if (quiz == null) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Quiz not found.")
        } else {
            if (answer.answer == quiz.answer) rightAnswerResponse else wrongAnswerResponse
        }
    }

    fun getQuiz(id: Int): Quiz? = quizRepository.findQuizById(id)

    fun createQuiz(newQuiz: NewQuizDTO, creatorEmail: String): Quiz {
//      입력 검증
        if (newQuiz.title.isNullOrEmpty()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required.")
        }

        if (newQuiz.options.isNullOrEmpty()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Options cannot be empty.")
        }
//      퀴즈 객체 생성
        val quiz = Quiz(
            title = newQuiz.title,
            text = newQuiz.text,
            options = newQuiz.options,
            answer = newQuiz.answer ?: listOf(),
            creatorEmail = creatorEmail
        )
//      퀴즈 저장
        val savedQuiz = quizRepository.save(quiz)
//      저장된 퀴즈 반환
        return Quiz(
            id = savedQuiz.id,
            title = savedQuiz.title,
            text = savedQuiz.text,
            options = savedQuiz.options
        )
    }

    fun getAllQuizzes(): List<Quiz> =
        quizRepository.findAll()

    enum class QuizDeleteResult {
        SUCCESS, NOT_FOUND, UNAUTHORIZED
    }

    fun deleteQuiz(id: Int, userEmail: String): QuizDeleteResult {
        val quiz = quizRepository.findById(id).orElse(null) ?: return QuizDeleteResult.NOT_FOUND

        if(quiz.creatorEmail != userEmail) return QuizDeleteResult.UNAUTHORIZED // 퀴즈 생성자의 이메일과 현재 사용자의 이메일을 비교합니다.

        quizRepository.delete(quiz)
        return QuizDeleteResult.SUCCESS
    }

}