package engine.service

import engine.dto.UserDTO
import engine.entity.User
import engine.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(private val userRepository: UserRepository, private val encoder: PasswordEncoder) {

    fun registerUser(body: UserDTO) {
        val existingUser = userRepository.findByEmail(body.email).orElse(null)

        if (existingUser != null) {
            throw UserAlreadyExistsException("User with email ${body.email} already exists.")
        }

        userRepository.save(
            User(
                email = body.email,
                password = encoder.encode(body.password),
                role = "ROLE_USER"
            )
        )
    }
}

class UserAlreadyExistsException(message: String) : RuntimeException(message)
