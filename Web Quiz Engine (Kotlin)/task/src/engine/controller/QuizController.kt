package engine.controller

@RestController
@RequestMapping("/api/quizzes")
class QuizController(private val quizService: QuizService) {

    @PostMapping("/{id}/solve")
    fun solveQuiz(@RequestBody answer: AnswerDTO, @PathVariable id: Int): ResponseEntity<ResponseDTO> =
        ResponseEntity.ok()
            .body(quizService.solveQuiz(id, answer) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND))

    @GetMapping("/{id}")
    fun getQuiz(@PathVariable id: Int): ResponseEntity<QuizDTO> =
        ResponseEntity.ok()
            .body(quizService.getQuiz(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND))

    @PostMapping
    fun createQuiz(@Valid @RequestBody newQuiz: NewQuizDTO): ResponseEntity<QuizDTO> =
        ResponseEntity.ok().body(quizService.createQuiz(newQuiz))

    @GetMapping
    fun getAllQuizzes(): ResponseEntity<ArrayList<QuizDTO>> =
        ResponseEntity.ok().body(quizService.getAllQuizzes())

}