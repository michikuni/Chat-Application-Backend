package org.company.chatapp.DTO

import org.company.chatapp.entity.FriendsEntity
import org.company.chatapp.entity.UserEntity
import java.time.Instant

data class FriendsDTO(
    val createdAt: Instant = Instant.now(),
    val status: FriendshipStatus,
    val friendId: Long,
    val userId: Long,
)

enum class FriendshipStatus {
    PENDING,
    ACCEPTED,
    BLOCKED,
    DECLINED
}

