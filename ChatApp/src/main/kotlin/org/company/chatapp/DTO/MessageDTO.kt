package org.company.chatapp.DTO

import java.sql.Timestamp

data class MessageDTO(
    val conversationId: GetConversation,
    val senderId: UserDTO,
    val content: String?,
    val mediaFile: String?,
    val createdAt: Timestamp,
    val isRead: Boolean
)
