package org.company.chatapp.DTO

import org.company.chatapp.entity.FriendsEntity
import org.company.chatapp.entity.UserEntity
import java.time.Instant

data class FriendsDTO(
    val userId: UserEntity,
    val friendId: UserEntity,
    val status: FriendshipStatus,
    val createdAt: Instant = Instant.now(),
)

enum class FriendshipStatus {
    PENDING,
    ACCEPTED,
    BLOCKED,
    DECLINED
}

fun FriendsDTO.toFriendsEntity(): FriendsEntity {
    return FriendsEntity(
        user = this.userId,
        friend = this.friendId,
        status = FriendshipStatus.PENDING,
        createdAt = this.createdAt,
    )
}
fun FriendsDTO.toFriendsAcceptedEntity(): FriendsEntity {
    return FriendsEntity(
        user = this.userId,
        friend = this.friendId,
        status = FriendshipStatus.ACCEPTED,
        createdAt = this.createdAt,
    )
}
fun FriendsDTO.toFriendsRejectedEntity(): FriendsEntity {
    return FriendsEntity(
        user = this.userId,
        friend = this.friendId,
        status = FriendshipStatus.DECLINED,
        createdAt = this.createdAt,
    )
}
