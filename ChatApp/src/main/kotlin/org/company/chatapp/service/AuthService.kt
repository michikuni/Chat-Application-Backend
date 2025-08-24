package org.company.chatapp.service

import org.company.chatapp.DTO.LoginDTO
import org.company.chatapp.DTO.LoginResponseDTO
import org.company.chatapp.DTO.RegisterDTO
import org.company.chatapp.DTO.toEntity
import org.company.chatapp.entity.UserEntity
import org.company.chatapp.repository.UserRepository
import org.company.chatapp.utils.JwtUtils
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService (
    private val userRepository: UserRepository,
    private val jwtUtils: JwtUtils,
    private val passwordEncoder: BCryptPasswordEncoder
){
    fun register(userDTO: RegisterDTO): UserEntity {
        if (userRepository.existsByUsername(userDTO.username)) {
            throw IllegalArgumentException("Tài khoản ${userDTO.username} đã tồn tại")
        }
        if (userRepository.existsByEmail(userDTO.email)) {
            throw IllegalArgumentException("Email ${userDTO.email} đã tồn tại")
        }
        val user = userDTO.toEntity().apply {
            password = passwordEncoder.encode(userDTO.password)
        }
        return userRepository.save(user)
    }

    fun login (login: LoginDTO): LoginResponseDTO {
        val user = userRepository.findByUsername(login.username)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại")
        if(passwordEncoder.matches(login.password, user.password)) {
            return LoginResponseDTO(
                id = user.id,
                username = user.username,
                token = jwtUtils.generateToken(user.username)
            )
        } else {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Tài khoản hoặc mật khẩu không đúng")
        }
    }
}