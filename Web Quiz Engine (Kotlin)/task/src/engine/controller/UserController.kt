package engine.controller

import engine.dto.UserDTO
import engine.service.UserAlreadyExistsException
import engine.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class UserController(private val userService: UserService) {

    // 사용자 등록
    @PostMapping("/register")
    fun registerUser(@RequestBody @Valid body: UserDTO): ResponseEntity<String> {
        return try {
            userService.registerUser(body) // 사용자 등록 서비스 호출
            ResponseEntity.ok("User registered successfully.") // 사용자 등록 성공 시 OK(200) 응답 반환
        } catch (e: UserAlreadyExistsException) {
            ResponseEntity.badRequest().body(e.message) // 이미 존재하는 사용자일 경우 BAD REQUEST(400) 응답 반환
        }
    }
}

