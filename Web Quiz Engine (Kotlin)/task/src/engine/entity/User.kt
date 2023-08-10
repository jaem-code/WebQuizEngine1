package engine.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_generator")
    @SequenceGenerator(name = "user_seq_generator", sequenceName = "user_sequence", allocationSize = 1)
    val id: Long? = null,

    @Column(unique = true)
    val email: String,

    val password: String,

    val role: String,
) {
    private constructor() : this(null, "", "", "")
}