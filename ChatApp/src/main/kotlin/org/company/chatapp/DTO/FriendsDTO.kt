package org.company.chatapp.DTO

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

