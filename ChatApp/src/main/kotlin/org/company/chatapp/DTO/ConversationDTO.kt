package org.company.chatapp.DTO

import java.time.Instant

data class ConversationDTO(
    val memberIds: List<Long>,
    val conversationName: String?,
    val avatar: String?,
    val numberMembers: Int,
    val messages: List<MessageDTO>,
    val createAt: Instant,
)

data class CreateConversation(
    val friendId: Long,
    val message: String
)