package org.company.chatapp.service

import org.company.chatapp.DTO.FriendsDTO
import org.company.chatapp.DTO.FriendshipStatus
import org.company.chatapp.entity.FriendsEntity
import org.company.chatapp.entity.UserEntity
import org.company.chatapp.repository.FriendshipRepository
import org.company.chatapp.repository.UserRepository
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@Service
class FriendService(
    private val friendshipRepository: FriendshipRepository,
    private val userService: UserService,
    private val customMapper: CustomMapper,
    private val userRepository: UserRepository
){
    fun sendFriendRequest(senderId: Long, receiverEmail: String): FriendsEntity {
        val sender = userService.getUserById(senderId)
            ?: throw IllegalArgumentException("Không tìm thấy người gửi")

        val receiver = userService.getUserByEmail(receiverEmail)
            ?: throw IllegalArgumentException("Không tìm thấy người nhận")

        if (friendshipRepository.findBetweenUsers(senderId, receiver.id) != null) {
            throw IllegalArgumentException("Đã gửi lời mời hoặc đã là bạn bè")
        }
        return friendshipRepository.save(
            FriendsEntity(user = sender, friend = receiver, status = FriendshipStatus.PENDING, createdAt = Instant.now())
        )
    }

    fun acceptFriendRequest(friendshipId: Long): FriendsEntity {
        val friendship = friendshipRepository.findById(friendshipId)
            .orElseThrow { IllegalArgumentException("Không tìm thấy lời mời kết bạn") }

        val updated = friendship.copy(status = FriendshipStatus.ACCEPTED)
        return friendshipRepository.save(updated)
    }

    fun getAllPendingFriends(userId :Long): List<FriendsDTO>{
        val listPendingId = friendshipRepository.findPendingFriendIdByUserId(userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy lời mời kết bạn nào cho user id : $userId")
        return listPendingId.mapNotNull { id ->
            val friendshipId  = friendshipRepository.findBetweenUsers(userId, id)
            userRepository.findById(id).map { friendshipId?.let { it1 -> customMapper.friendshipDto(userEntity = it, friendshipId = it1.id) } }.orElse(null)
        }
    }

    fun getAllRequestedFriends(userId :Long): List<FriendsDTO>{
        val listRequestedId = friendshipRepository.findRequestedFriendIdByUserId(userId)
            ?:throw ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy yêu cầu kết bạn nào cho user id: $userId")
        return listRequestedId.mapNotNull { id ->
            val friendshipId = friendshipRepository.findBetweenUsers(userId, id)
            userRepository.findById(id).map { friendshipId?.let { it1 -> customMapper.friendshipDto(userEntity = it, friendshipId = it1.id) } }.orElse(null)
        }
    }

    fun getFriendByEmail(userId: Long, email: String): FriendsDTO?{
        val friend = userRepository.findByEmail(email)
        val friendship = friend?.let { friendshipRepository.findBetweenUsers(userId, friend.id) }
        var friendshipId = 0L
        if (friendship != null){
            friendshipId = friendship.id
        }
        return friend?.let { customMapper.friendshipDto(userEntity = it, friendshipId = friendshipId) }
    }

    fun getFriendship(id: Long): Long{
        val friendship = friendshipRepository.findById(id)
            .orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: $id") }
        return friendship.user.id
    }


}