package org.company.chatapp.service

import org.company.chatapp.DTO.*
import org.company.chatapp.entity.UserEntity
import org.company.chatapp.repository.FriendshipRepository
import org.company.chatapp.repository.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val friendshipRepository: FriendshipRepository,
    private val customMapper: CustomMapper
){
    fun getAllFriendsById(userId: Long): List<UserDTO> {
        val friendshipId = friendshipRepository.findAllFriendshipIdByUserId(userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy bạn bè nào cho user id : $userId")
        val friendship: MutableList<Long> = mutableListOf()
        for (fr in friendshipId){
            val friend = friendshipRepository.findFriendshipById(fr)
            if (friend != null) {
                if (userId == friend.user.id) {
                    friendship.add(friend.friend.id)
                } else if (userId == friend.friend.id) {
                    friendship.add(friend.user.id)
                }
            }
        }
        return friendship.mapNotNull { id ->
            userRepository.findById(id).map { customMapper.userToDto(it) }.orElse(null)
        }
    }

    fun updateAvatar(userId: Long, avatarUrl:String): UserEntity{
        val user = userRepository.findById(userId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "User not found") }
        val userData = user.copy(avatar = avatarUrl)
        return userRepository.save(userData)
    }

    fun getUserInfo(userId: Long): UserDTO {
        val user = userRepository.findById(userId)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "User not found") }
        return customMapper.userToDto(user)
    }

    fun getAvatar(userId: Long): String? {
        val user = userRepository.findById(userId)
        .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "User not found") }
        return user.avatar
    }

    fun getAllUser(): List<UserEntity> {
        return userRepository.findAll()
    }

    fun getUserById(id: Long): UserEntity? {
        return userRepository.findById(id)
            .orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với id: $id") }
    }

    fun getUserByEmail(email: String): UserEntity? {
        return userRepository.findByEmail(email)
    }
    fun getUserByUsername(username: String): UserEntity? {
        return userRepository.findByUsername(username)
    }
}