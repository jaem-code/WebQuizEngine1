package engine.service

import org.springframework.stereotype.Service

@Service
class QuizService {

    private val rightAnswerResponse: ResponseDTO =
        ResponseDTO(true, "Congratulations, you're right!")

    private val wrongAnswerResponse: ResponseDTO =
        ResponseDTO(false, "Wrong answer! Please, try again.")

    private var quizId: Int = 1
    private val quizList: ArrayList<QuizDTO> = ArrayList()

    fun solveQuiz(id: Int, answer: AnswerDTO): ResponseDTO? {
        val quiz: QuizDTO? = getQuiz(id)

        return if (quiz == null) null else {
            if (answer.answer == quiz.answer) rightAnswerResponse else wrongAnswerResponse
        }
    }

    fun getQuiz(id: Int): QuizDTO? = quizList.find { it.id == id }

    fun createQuiz(newQuiz: NewQuizDTO): QuizDTO {
        val quiz = QuizDTO(
            id = quizId,
            title = newQuiz.title,
            text = newQuiz.text,
            options = newQuiz.options,
            answer = newQuiz.answer ?: ArrayList()
        )

        quizList.add(quiz)
        quizId++

        return quiz
    }

    fun getAllQuizzes(): ArrayList<QuizDTO> = quizList

}