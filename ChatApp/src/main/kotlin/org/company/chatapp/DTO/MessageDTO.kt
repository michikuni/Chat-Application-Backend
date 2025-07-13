package org.company.chatapp.DTO

import org.company.chatapp.entity.MessageEntity
import java.time.Instant

data class MessageDTO(
    val conversationId: Long,
    val senderId: Long,
    val content: String,
    val createdAt: Instant,
    val isRead: Boolean,
)

fun toMessageDTO(entity: MessageEntity): MessageDTO =
    MessageDTO(
        conversationId = entity.conversation.id,
        senderId = entity.sender.id,
        content = entity.content,
        createdAt = entity.createdAt,
        isRead = entity.isRead
    )
