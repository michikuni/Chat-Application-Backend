package org.company.chatapp.service

import org.company.chatapp.DTO.RegisterRequestDTO
import org.company.chatapp.DTO.toEntity
import org.company.chatapp.entity.UserEntity
import org.company.chatapp.repository.UserRepository
import org.company.chatapp.utils.JwtUtils
import org.modelmapper.ModelMapper
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val jwtUtils: JwtUtils
){
    fun register(userDTO: RegisterRequestDTO): UserEntity {
        if (userRepository.existsByUsername(userDTO.username)) {
            throw IllegalArgumentException("Username ${userDTO.username} already exists")
        }
        if (userRepository.existsByEmail(userDTO.email)) {
            throw IllegalArgumentException("Email ${userDTO.email} already exists")
        }
        val user = userDTO.toEntity().apply {
            password = passwordEncoder.encode(userDTO.password)
        }
        return userRepository.save(user)
    }

    fun login (username: String, password: String): String {
        val user = userRepository.findByUsername(username)
        ?: throw UsernameNotFoundException("User not found")
        if(!passwordEncoder.matches(password, user.password)) {
            throw Exception("Invalid password")
        }
        return jwtUtils.generateToken(username)
    }
}