package org.company.chatapp.service

import org.company.chatapp.DTO.*
import org.company.chatapp.entity.UserEntity
import org.company.chatapp.repository.FriendshipRepository
import org.company.chatapp.repository.UserRepository
import org.company.chatapp.utils.JwtUtils
import org.springframework.data.crossstore.ChangeSetPersister
import org.springframework.http.HttpStatus
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val friendshipRepository: FriendshipRepository,
    private val jwtUtils: JwtUtils,
    private val customMapper: CustomMapper
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
        ?: throw NullPointerException("Tài khoản không tồn tại")
        if(passwordEncoder.matches(login.password, user.password)) {
            return LoginResponseDTO(
                id = user.id,
                username = user.username,
                token = jwtUtils.generateToken(user.username)
            )
        } else {
            throw Exception("Tài khoản hoặc mật khẩu không đúng")
        }
    }

    fun getAllFriendsById(userId: Long): List<UserDTO> {
        val friendId = friendshipRepository.findAllFriendIdByUserId(userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy bạn bè nào cho user id : $userId")

        return friendId.mapNotNull { id ->
            userRepository.findById(id).map { customMapper.toDto(it) }.orElse(null)
        }
    }

    fun getAllUser(): List<UserEntity> {
        return userRepository.findAll()
    }

    fun getUserById(id: Long): UserEntity? {
        return userRepository.findById(id)
            .orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với id: $id") }
    }
    fun getUserByUsername(username: String): UserEntity? {
        return userRepository.findByUsername(username)
    }
}