package org.company.chatapp.DTO

import java.time.Instant

data class MessageDTO(
    val conversationId: Long,
    val senderId: Long,
    val content: String,
    val createdAt: Instant,
    val isRead: Boolean
)
