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

    @PostMapping("/register")
    fun registerUser(@RequestBody @Valid body: UserDTO): ResponseEntity<String> {
        try {
            userService.registerUser(body)
            return ResponseEntity.ok("User registered successfully.")
        } catch (e: UserAlreadyExistsException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        }
    }
}
