package engine.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true)
    val email: String,

    val password: String,

    val role: String,
) {
    private constructor() : this(null, "", "", "")
}