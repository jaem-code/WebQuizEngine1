package engine.repository

import engine.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String?): Optional<User>

    fun existsByEmail(email: String): Boolean // existsByEmail 메서드 추가
}