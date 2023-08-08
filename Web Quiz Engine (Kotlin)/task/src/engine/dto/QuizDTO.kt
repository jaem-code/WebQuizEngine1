package engine.dto

import com.fasterxml.jackson.annotation.JsonIgnore

data class QuizDTO(
    val id: Int,
    val title: String,
    val text: String,
    val options: ArrayList<String>,
    @JsonIgnore val answer: ArrayList<Int>
)