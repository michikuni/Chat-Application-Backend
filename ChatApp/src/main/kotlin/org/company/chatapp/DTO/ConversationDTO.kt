package org.company.chatapp.DTO

import org.company.chatapp.entity.ConversationEntity
import java.time.Instant

data class ConversationDTO(
    val memberIds: List<Long>,
    val conversationName: String?,
    val avatar: String?,
    val numberMembers: Int,
    val createAt: Instant = Instant.now(),
    val lastMessage: Long?
)
fun ConversationDTO.toEntityGroup(): ConversationEntity {
    return ConversationEntity(
        memberIds = memberIds,
        conversationName = conversationName,
        avatar = avatar,
        numberMembers = numberMembers,
        createdAt = createAt,
        lastMessage = lastMessage
    )
}