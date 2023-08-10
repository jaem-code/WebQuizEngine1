package engine.service

import engine.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(user: User) : UserDetails {

    private val username: String
    private val password: String
    private val rolesAndAuthorities: List<GrantedAuthority>

    // 생성자에서 사용자 정보를 받아 초기화합니다.
    init {
        username = user.email
        password = user.password
        rolesAndAuthorities = listOf<GrantedAuthority>(SimpleGrantedAuthority(user.role))
    }

    // 사용자의 권한 목록을 반환합니다.
    override fun getAuthorities() = rolesAndAuthorities

    // 사용자의 비밀번호를 반환합니다.
    override fun getPassword() = password

    // 사용자의 아이디(이메일)를 반환합니다.
    override fun getUsername() = username

    // 계정이 만료되지 않았음을 반환합니다.
    override fun isAccountNonExpired() = true

    // 계정이 잠기지 않았음을 반환합니다.
    override fun isAccountNonLocked() = true

    // 자격 증명이 만료되지 않았음을 반환합니다.
    override fun isCredentialsNonExpired() = true

    // 계정이 활성화되어 있음을 반환합니다.
    override fun isEnabled() = true
}