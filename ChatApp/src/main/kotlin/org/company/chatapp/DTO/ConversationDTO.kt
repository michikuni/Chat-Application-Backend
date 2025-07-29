package org.company.chatapp.DTO

import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime

data class ConversationDTO(
    val id: Long,
    val userId: Long,
    val name: String,
    val avatar: String,
    val content: String,
    val createdAt: Timestamp,
    val isRead: Boolean,
)

data class CreateConversation(
    val friendId: Long,
    val message: String
)