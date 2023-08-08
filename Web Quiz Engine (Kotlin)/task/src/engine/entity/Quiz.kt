package engine.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode

@Entity
@Table(name = "quizzes")
@SequenceGenerator(name = "quizzes_generator", allocationSize = 1)
data class Quiz(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "quizzes_generator")
    @Column(updatable = false)
    val id: Int? = null,

    val title: String? = null,

    val text: String? = null,

    @Fetch(value = FetchMode.SUBSELECT)
    @ElementCollection(fetch = FetchType.EAGER)
    val options: List<String>? = null,

    @Fetch(value = FetchMode.SUBSELECT)
    @ElementCollection(fetch = FetchType.EAGER)
    @JsonIgnore
    val answer: List<Int>? = emptyList(),

)