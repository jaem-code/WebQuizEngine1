package engine.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    private val userDetailsService: UserDetailsService
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http {
            securityMatcher("/api/quizzes/**")  // "/api/quizzes/**" 패턴에 대한 보안 설정
            csrf { disable() }  // CSRF 보안 비활성화
            authorizeHttpRequests {
                authorize("/register", permitAll)
                authorize(anyRequest, authenticated) // 모든 요청에 대해 "USER" 권한이 필요
                authorize("/actuator/shutdown", permitAll)  // Allow access without authentication
            }
            httpBasic { }  // HTTP Basic 인증 사용
        }
        return http.build()  // SecurityFilterChain을 반환
    }

    @Bean
    fun authenticationManager(passwordEncoder: BCryptPasswordEncoder): AuthenticationManager =
        ProviderManager(DaoAuthenticationProvider().apply {
            setUserDetailsService(userDetailsService)
            setPasswordEncoder(passwordEncoder)
        })

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()  // BCryptPasswordEncoder를 사용한 비밀번호 인코더 반환
}
