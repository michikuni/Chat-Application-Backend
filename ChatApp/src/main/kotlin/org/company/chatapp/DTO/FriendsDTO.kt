package org.company.chatapp.DTO

import org.company.chatapp.entity.FriendsEntity
import org.company.chatapp.entity.UserEntity
import java.time.Instant

data class FriendsDTO(
    val friendshipId: Long,
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val avatar: String? = null
)

enum class FriendshipStatus {
    PENDING,
    ACCEPTED,
    BLOCKED,
    DECLINED
}

