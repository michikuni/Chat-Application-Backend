package org.company.chatapp.controller

import org.company.chatapp.DTO.LoginDTO
import org.company.chatapp.DTO.RegisterDTO
import org.company.chatapp.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService
) {
    @PostMapping("/register")
    fun register(
        @RequestBody registerDTO: RegisterDTO
    ): ResponseEntity<String> {
        return try {
            userService.register(registerDTO)
            ResponseEntity.ok("Đăng ký tài khoản ${registerDTO.username} thành công")
        } catch (e: IllegalArgumentException){
            ResponseEntity.badRequest().body(e.message)
        }
    }
    @GetMapping("/test")
    fun test(): String = "OK"

    @PostMapping("/login")
    fun login(@RequestBody loginRequestDTO: LoginDTO): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(userService.login(loginRequestDTO))
        } catch (e: NullPointerException){
            ResponseEntity.badRequest().body(e.message)
        } catch (e: Exception){
            ResponseEntity.badRequest().body(e.message)
        }
    }
}