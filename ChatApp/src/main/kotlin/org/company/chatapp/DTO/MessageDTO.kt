package org.company.chatapp.DTO

import org.company.chatapp.entity.ConversationEntity
import java.time.Instant

data class MessageDTO(
    val conversationId: ConversationEntity,
    val senderId: UserDTO,
    val content: String,
    val createdAt: Instant,
    val isRead: Boolean
)
