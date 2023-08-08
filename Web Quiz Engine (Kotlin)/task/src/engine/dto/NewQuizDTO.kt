package engine.dto

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class NewQuizDTO(
    @field:NotBlank val title: String,
    @field:NotBlank val text: String,
    @field:Size(min = 2) val options: ArrayList<String>,
    val answer: ArrayList<Int>?
)