package org.company.chatapp.controller

import org.company.chatapp.DTO.LoginRequestDTO
import org.company.chatapp.DTO.RegisterRequestDTO
import org.company.chatapp.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.math.log

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService
) {
    @PostMapping("/register")
    fun register(
        @RequestBody userDTO: RegisterRequestDTO
    ): ResponseEntity<String> {
        return try {
            userService.register(userDTO)
            ResponseEntity.ok("Register for user ${userDTO.username} successfully")
        } catch (e: Exception){
            ResponseEntity.badRequest().body(e.message)
        }
    }
    @GetMapping("/test")
    fun test(): String = "OK"

    @PostMapping("/login")
    fun login(
        @RequestBody loginRequestDTO: LoginRequestDTO
    ): ResponseEntity<Map<String, String>> {
        try {
            val token = userService.login(loginRequestDTO.username, loginRequestDTO.password)
            return ResponseEntity.ok(mapOf("token" to token))
        } catch (e: Exception){
            return ResponseEntity.badRequest().body(mapOf("Error:" to "Failed ${e.message}"))
        }
    }
}