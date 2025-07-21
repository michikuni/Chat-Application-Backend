package org.company.chatapp.service

import org.company.chatapp.DTO.*
import org.company.chatapp.entity.UserEntity
import org.company.chatapp.repository.UserRepository
import org.company.chatapp.utils.JwtUtils
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val jwtUtils: JwtUtils
){
    fun register(userDTO: RegisterDTO): UserEntity {
        if (userRepository.existsByUsername(userDTO.username)) {
            throw IllegalArgumentException("Account ${userDTO.username} already exists")
        }
        if (userRepository.existsByEmail(userDTO.email)) {
            throw IllegalArgumentException("Email ${userDTO.email} already exists")
        }
        val user = userDTO.toEntity().apply {
            password = passwordEncoder.encode(userDTO.password)
        }
        return userRepository.save(user)
    }

    fun login (login: LoginDTO): LoginResponseDTO {
        val user = userRepository.findByUsername(login.username)
        ?: throw UsernameNotFoundException("User not found")
        if(passwordEncoder.matches(login.password, user.password)) {
            return LoginResponseDTO(
                username = user.username,
                token = jwtUtils.generateToken(user.username)
            )
        } else {
            throw Exception("Invalid password: Check in server UserService.")
        }
    }

    fun getUserById(id: Long): UserEntity {
        return userRepository.findById(id)
            .orElseThrow { RuntimeException("User not found with id $id") }
    }
    fun getUserByUsername(username: String): UserEntity? {
        return userRepository.findByUsername(username)
    }
}