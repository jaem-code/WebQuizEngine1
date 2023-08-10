package engine.service

import engine.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository
) : UserDetailsService {
    // 주어진 username으로부터 사용자 정보를 가져오는 메서드
    override fun loadUserByUsername(username: String): UserDetails {
        // 사용자 정보 조회
        val user = userRepository.findByEmail(username)
            .map { UserDetailsImpl(it) } // Optional<User>를 UserDetailsImpl로 매핑
            .orElseThrow { UsernameNotFoundException("User not found with username: $username") }

        return user
    }
}