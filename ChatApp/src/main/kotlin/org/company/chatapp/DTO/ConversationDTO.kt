package org.company.chatapp.DTO

import org.company.chatapp.entity.ConversationEntity
import java.time.Instant

data class ConversationDTO(
    val memberIds: List<Long>,
    val conversationName: String?,
    val avatar: String?,
    val numberMembers: Int?,
    val createAt: Instant,
    val lastMessage: Long?,
)
fun toConversationDTO(
    entity: ConversationEntity
): ConversationDTO =
    ConversationDTO(
        memberIds = entity.memberIds,
        conversationName = entity.conversationName,
        avatar = entity.avatar,
        numberMembers = entity.numberMembers,
        createAt = entity.createdAt,
        lastMessage = entity.lastMessage,
    )