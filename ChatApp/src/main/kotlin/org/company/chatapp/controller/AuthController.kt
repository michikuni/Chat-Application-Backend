package org.company.chatapp.controller

import jakarta.servlet.http.HttpServletRequest
import org.company.chatapp.DTO.LoginDTO
import org.company.chatapp.DTO.RegisterDTO
import org.company.chatapp.service.AuthService
import org.company.chatapp.utils.JwtUtils
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtUtils: JwtUtils
) {
    @PostMapping("/register")
    fun register(
        @RequestBody registerDTO: RegisterDTO
    ): ResponseEntity<String> {
        return try {
            authService.register(registerDTO)
            ResponseEntity.ok("Đăng ký tài khoản ${registerDTO.username} thành công")
        } catch (e: IllegalArgumentException){
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequestDTO: LoginDTO
    ): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(authService.login(loginRequestDTO))
        } catch (e: ResponseStatusException){
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @GetMapping("/checkTokenValid")
    fun checkTokenValid(request: HttpServletRequest,): ResponseEntity<Any> {
        try {
            val token = request.getHeader("Authorization").removePrefix("Bearer ")
            println("OKOK $token")
            val username = jwtUtils.extractUsername(token)
            println("username: $username")
            if (username?.let { jwtUtils.validateToken(token = token, user = it) } == true) {
                return ResponseEntity.ok(mapOf(
                    "valid" to true,
                    "username" to username
                ))
            }
        } catch (ex: Exception) {
            return ResponseEntity.status(401).body(mapOf(
                "valid" to false,
                "error" to "Token invalid or expired"
            ))
        }
        return ResponseEntity.status(401).body(mapOf("valid" to false))
    }
}