package org.company.chatapp.DTO

import java.time.Instant

data class ConversationDTO(
    val memberIds: List<Long>,
    val conversationName: String?,
    val avatar: String?,
    val numberMembers: Int,
    val createAt: Instant = Instant.now(),
)

data class createConversation(
    val friendId: Long,
    val message: String
)