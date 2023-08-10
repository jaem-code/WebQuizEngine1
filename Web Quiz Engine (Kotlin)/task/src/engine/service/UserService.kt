package engine.service

import engine.dto.UserDTO
import engine.entity.User
import engine.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder
) {

    // 사용자 등록
    fun registerUser(body: UserDTO) {
        // 이미 존재하는 사용자인지 확인
        if (userRepository.existsByEmail(body.email)) {
            throw UserAlreadyExistsException("User with email ${body.email} already exists.") // 이미 존재하는 사용자일 경우 예외 발생
        }

        // 비밀번호를 해시하여 저장
        val hashedPassword = encoder.encode(body.password)

        // 새 사용자 정보 저장
        userRepository.save(
            User(
                email = body.email,
                password = hashedPassword,
                role = "ROLE_USER"
            )
        )
    }
}

// 이미 존재하는 사용자 예외 클래스
class UserAlreadyExistsException(message: String) : RuntimeException(message)
