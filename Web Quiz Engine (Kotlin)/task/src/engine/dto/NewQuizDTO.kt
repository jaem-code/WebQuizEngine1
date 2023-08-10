package engine.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.validation.constraints.NotNull
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class NewQuizDTO(
    @field:NotBlank
    val title: String,

    @field:NotBlank
    val text: String,

    @field:Size(min = 2)
    val options: List<String>,

    val answer: ArrayList<Int>?
)