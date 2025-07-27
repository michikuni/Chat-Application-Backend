package org.company.chatapp.service

import org.company.chatapp.DTO.ConversationDTO
import org.company.chatapp.DTO.FriendsDTO
import org.company.chatapp.DTO.MessageDTO
import org.company.chatapp.DTO.UserDTO
import org.company.chatapp.entity.ChatMemberEntity
import org.company.chatapp.entity.ConversationEntity
import org.company.chatapp.entity.MessageEntity
import org.company.chatapp.entity.UserEntity
import org.springframework.stereotype.Service
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
    fun conversationDto(conversationEntity: ConversationEntity): ConversationDTO = ConversationDTO(
        conversationName = conversationEntity.conversationName,
        memberIds = conversationEntity.memberIds,
        avatar = conversationEntity.avatar,
        createAt = conversationEntity.createdAt,
        numberMembers = conversationEntity.numberMembers
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

    fun chatMembersEntity(userId: UserEntity, conversationId: ConversationEntity): ChatMemberEntity = ChatMemberEntity(
        conversation = conversationId,
        userId = userId,
    )
}