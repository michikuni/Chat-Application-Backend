package org.company.chatapp.DTO

import org.company.chatapp.entity.MessageEntity
import java.time.Instant

data class MessageDTO(
    val id: Long,
    val senderId: Long,
    val text: String,
    val timestamp: Instant,
    val isRead: Boolean,
)
fun toMessageDTO(entity: MessageEntity): MessageDTO =
    MessageDTO(
        id = entity.id,
        senderId = entity.sender.id,
        text = entity.text,
        timestamp = entity.timestamp,
        isRead = entity.isRead
    )