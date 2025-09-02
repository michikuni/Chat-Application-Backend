package org.company.chatapp.DTO

import java.sql.Timestamp

data class ConversationDTO(
    val id: Long,
    val userId: Long,
    val senderId: Long,
    val name: String,
    val avatar: String,
    val content: String?,
    val mediaFile: String?,
    val createdAt: Timestamp,
    val isRead: Boolean,
    val themeColor: String?,
    val conversationType: ConversationType?
)

data class CreateConversation(
    val friendId: Long,
    val message: String
)

data class GetConversation(
    val id: Long,
    val memberIds: List<Long>,
    val conversationName: String?,
    val avatar: String?,
    val numberMembers: Int,
    val createdAt: Timestamp,
)

enum class ConversationType{
    PAIR,
    GROUP
}