package org.company.chatapp.service

import org.company.chatapp.DTO.FriendsDTO
import org.company.chatapp.DTO.FriendshipStatus
import org.company.chatapp.DTO.UserDTO
import org.company.chatapp.entity.FriendsEntity
import org.company.chatapp.repository.FriendshipRepository
import org.company.chatapp.repository.UserRepository
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
    fun sendFriendRequest(senderId: Long, receiverEmail: String) {
        val sender = userService.getUserById(senderId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người gửi")

        val receiver = userService.getUserByEmail(receiverEmail)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người nhận")

        val friendships = friendshipRepository.findBetweenUsers(senderId, receiver.id)
        val friendship = friendships.firstOrNull()

        if (friendship != null) {
            when (friendship.status) {
                FriendshipStatus.ACCEPTED, FriendshipStatus.PENDING -> {
                    throw IllegalArgumentException("Đã gửi lời mời hoặc đã là bạn bè")
                }
                FriendshipStatus.DECLINED -> {
                    if (friendship.user.id == senderId) {
                        // Người gửi cũ muốn gửi lại
                        val updated = friendship.copy(
                            status = FriendshipStatus.PENDING,
                            createdAt = Instant.now()
                        )
                        friendshipRepository.save(updated)
                    } else {
                        // Người kia từng từ chối -> tạo mới lời mời
                        friendshipRepository.save(
                            FriendsEntity(
                                user = sender,
                                friend = receiver,
                                status = FriendshipStatus.PENDING,
                                createdAt = Instant.now()
                            )
                        )
                    }
                }
                else -> {}
            }
        } else {
            // Chưa có quan hệ -> tạo mới
            friendshipRepository.save(
                FriendsEntity(
                    user = sender,
                    friend = receiver,
                    status = FriendshipStatus.PENDING,
                    createdAt = Instant.now()
                )
            )
        }
    }


    fun acceptFriendRequest(friendshipId: Long): FriendsEntity {
        val friendship = friendshipRepository.findById(friendshipId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy lời mời kết bạn") }

        val updated = friendship.copy(status = FriendshipStatus.ACCEPTED, createdAt = Instant.now())
        return friendshipRepository.save(updated)
    }

    fun cancelFriendRequest(friendshipId: Long): FriendsEntity {
        val friendship = friendshipRepository.findById(friendshipId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy id bạn bè") }
        val updated = friendship.copy(status = FriendshipStatus.DECLINED, createdAt = Instant.now())
        return friendshipRepository.save(updated)
    }

    fun getAllPendingFriends(userId: Long): List<FriendsDTO> {
        val listPendingIds = friendshipRepository.findPendingFriendIdByUserId(userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy lời mời kết bạn nào cho user id: $userId")

        return listPendingIds.mapNotNull { friendId ->
            // lấy danh sách quan hệ (nếu có trùng thì chỉ lấy bản ghi đầu tiên)
            val friendship = friendshipRepository.findBetweenUsers(userId, friendId).firstOrNull()
                ?: return@mapNotNull null

            userRepository.findById(friendId).map { user ->
                customMapper.friendshipDto(userEntity = user, friendshipId = friendship.id)
            }.orElse(null)
        }
    }


    fun getAllRequestedFriends(userId: Long): List<FriendsDTO> {
        val listRequestedIds = friendshipRepository.findRequestedFriendIdByUserId(userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy yêu cầu kết bạn nào cho user id: $userId")

        return listRequestedIds.mapNotNull { friendId ->
            val friendship = friendshipRepository.findBetweenUsers(userId, friendId).firstOrNull()
                ?: return@mapNotNull null

            userRepository.findById(friendId).map { user ->
                customMapper.friendshipDto(userEntity = user, friendshipId = friendship.id)
            }.orElse(null)
        }
    }


    fun getFriendByEmail(email: String): UserDTO?{
        val friend = userRepository.findByEmail(email) ?: return null
        return friend.let { customMapper.userToDto(it) }
    }

}