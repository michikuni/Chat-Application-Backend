package org.company.chatapp.service

import org.company.chatapp.DTO.*
import org.company.chatapp.entity.ConversationEntity
import org.company.chatapp.entity.MessageEntity
import org.company.chatapp.entity.UserEntity
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.Instant

@Service
class CustomMapper {
    fun userToDto(entity: UserEntity): UserDTO = UserDTO(
        id = entity.id,
        name = entity.name,
        username = entity.username,
        email = entity.email,
        avatar = entity.avatar
    )
    fun friendshipDto(userEntity: UserEntity, friendshipId: Long): FriendsDTO = FriendsDTO(
        friendshipId = friendshipId,
        id = userEntity.id,
        name = userEntity.name,
        username = userEntity.username,
        email = userEntity.email,
        avatar = userEntity.avatar
    )

    fun messageDto(messageEntity: MessageEntity): MessageDTO = MessageDTO(
        senderId = userToDto(messageEntity.senderId),
        content = messageEntity.content,
        createdAt = Timestamp.from(messageEntity.createdAt),
        conversationId = conversationDto(messageEntity.conversationId),
        isRead = messageEntity.isRead,
    )

    fun conversationDto(conversationEntity: ConversationEntity): GetConversation = GetConversation(
        id = conversationEntity.id,
        memberIds = conversationEntity.memberIds,
        conversationName = conversationEntity.conversationName,
        avatar = conversationEntity.avatar,
        numberMembers = conversationEntity.numberMembers,
        createdAt = Timestamp.from(conversationEntity.createdAt),
    )

    fun conversationEntity(conversationName: String?, avatar: String?, numberMembers: Int, memberIds: List<Long>, createdAt: Instant): ConversationEntity = ConversationEntity(
        avatar = avatar,
        numberMembers = numberMembers,
        conversationName = conversationName,
        memberIds = memberIds,
        createdAt = Instant.now()
    )

    fun messageEntity(conversationId: ConversationEntity, senderId: UserEntity, isSent: Boolean, content: String, createdAt: Instant): MessageEntity = MessageEntity(
        conversationId = conversationId,
        senderId = senderId,
        content = content,
        createdAt = Instant.now(),
        isRead = isSent,
    )

}