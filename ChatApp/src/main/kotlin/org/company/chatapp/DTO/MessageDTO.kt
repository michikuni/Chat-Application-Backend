package org.company.chatapp.DTO

import org.company.chatapp.entity.ConversationEntity
import org.company.chatapp.entity.UserEntity
import java.time.Instant

data class MessageDTO(
    val conversationId: ConversationEntity,
    val senderId: UserEntity,
    val content: String,
    val createdAt: Instant,
    val isRead: Boolean
)
