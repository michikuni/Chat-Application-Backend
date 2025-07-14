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
        if (userRepository.existsByAccount(userDTO.account)) {
            throw IllegalArgumentException("Account ${userDTO.account} already exists")
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
        val user = userRepository.findByAccount(login.account)
        ?: throw UsernameNotFoundException("User not found")
        if(passwordEncoder.matches(login.password, user.password)) {
            return LoginResponseDTO(
                account = user.account,
                token = jwtUtils.generateToken(user.account)
            )
        } else {
            throw Exception("Invalid password: Check in server UserService.")
        }
    }
}