package org.company.chatapp.service

import org.company.chatapp.DTO.ConversationDTO
import org.company.chatapp.DTO.UserDTO
import org.company.chatapp.entity.ConversationEntity
import org.company.chatapp.repository.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@Service
class ConversationService(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val chatMembersRepository: ChatMembersRepository,
    private val userRepository: UserRepository,
    private val customMapper: CustomMapper
){
    fun getConversation(userId: Long, friendId: Long): ConversationDTO{
        val conversationId = conversationRepository.findConversationIdByMemberIds(userId = userId, friendId = friendId)
        val conversation = conversationRepository.findById(conversationId)
        return conversation.map { customMapper.conversationDto(it) }.orElse(null)
    }

    fun createConversation(userId: Long, friendId: Long, message: String){
        val user = userRepository.findById(userId).get()
        val friend = userRepository.findById(friendId).get()
        val conversation = conversationRepository.save(customMapper.conversationEntity(
            avatar = friend.avatar, numberMembers = 2,
            conversationName = friend.name,
            memberIds = listOf(user.id, friend.id), createdAt = Instant.now()))
        messageRepository.save(customMapper.messageEntity(
            createdAt = Instant.now(),
            conversationId = conversation,
            senderId = user,
            content = message,
            isSent = false))
        chatMembersRepository.save(customMapper.chatMembersEntity(conversationId = conversation, userId = user))
    }
    fun getConversationById(id: Long): ConversationEntity? {
        return conversationRepository.findById(id)
            .orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với id: $id") }
    }
}