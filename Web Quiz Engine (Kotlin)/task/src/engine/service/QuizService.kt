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

    fun createQuiz(newQuiz: NewQuizDTO): Quiz {
        if (newQuiz.title.isNullOrEmpty()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required.")
        }

        if (newQuiz.options.isNullOrEmpty()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Options cannot be empty.")
        }

        val quiz = Quiz(
            title = newQuiz.title,
            text = newQuiz.text,
            options = newQuiz.options,
            answer = newQuiz.answer ?: listOf()
        )

        val savedQuiz = quizRepository.save(quiz)
        return Quiz(
            id = savedQuiz.id,
            title = savedQuiz.title,
            text = savedQuiz.text,
            options = savedQuiz.options
        )
    }

    fun getAllQuizzes(): List<Quiz> =
        quizRepository.findAll()

}