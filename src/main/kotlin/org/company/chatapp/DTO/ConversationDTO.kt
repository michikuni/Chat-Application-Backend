package org.company.chatapp.DTO

import org.company.chatapp.entity.ConversationEntity
import java.time.Instant

data class ConversationDTO(
    val id: Long,
    val type: String,
    val name: String?,
    val avatar: String?,
    val lastMessage: MessageDTO?,
    val unreadCount: Int,
    val updatedAt: Instant,
)
fun toConversationDTO(
    entity: ConversationEntity,
    unreadCount: Int
): ConversationDTO =
    ConversationDTO(
        id = entity.id,
        type = entity.type,
        name = entity.name,
        avatar = entity.avatar,
        lastMessage = entity.lastMessage?.let { toMessageDTO(it) },
        unreadCount = unreadCount,
        updatedAt = entity.updatedAt
    )