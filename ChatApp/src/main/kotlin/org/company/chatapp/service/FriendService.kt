package org.company.chatapp.service

import org.company.chatapp.DTO.FriendshipStatus
import org.company.chatapp.entity.FriendsEntity
import org.company.chatapp.repository.FriendshipRepository
import org.company.chatapp.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class FriendService (
    private val friendshipRepository: FriendshipRepository,
    private val userService: UserService
){
    fun sendFriendRequest(senderId: Long, receiverId: Long): FriendsEntity {
        val sender = userService.getUserById(senderId)
        val receiver = userService.getUserById(receiverId)
        if (friendshipRepository.findBetweenUsers(senderId, receiverId) != null) {
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

    fun getFriendship(id: Long): Long{
        val friendship = friendshipRepository.findById(id)
            .orElseThrow { IllegalArgumentException("Không tìm thấy id friend") }
        return friendship.user.id
    }
}