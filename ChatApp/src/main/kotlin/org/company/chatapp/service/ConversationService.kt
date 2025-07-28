package org.company.chatapp.service

import org.company.chatapp.DTO.ConversationDTO
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
    fun getConversation(userId: Long, friendId: Long): List<ConversationDTO>? {
        val conversationId = conversationRepository.findConversationBetweenUsers(userId, friendId)
        val message = conversationId?.let { messageRepository.findMessageByConversationId(it) }
        val conversation = conversationId?.let { getConversationById(it) }
        return if (message != null && conversation != null){
            message.map {
                customMapper.conversationDto(conversationEntity = conversation, messageEntity = message)
            }
        } else {
            null
        }
    }


    fun createConversation(userId: Long, friendId: Long, message: String){
        val user = userRepository.findById(userId).get()
        val friend = userRepository.findById(friendId).get()
        val conversationId = conversationRepository.findConversationBetweenUsers(userId, friendId)
        if(conversationId == null){
            conversationRepository.save(customMapper.conversationEntity(
                avatar = friend.avatar, numberMembers = 2,
                conversationName = friend.name,
                memberIds = listOf(user.id, friend.id), createdAt = Instant.now()))
        }
        val conversation = conversationId?.let { conversationRepository.findById(it).get() }
        if(conversation != null){
            messageRepository.save(customMapper.messageEntity(
                createdAt = Instant.now(),
                conversationId = conversation,
                senderId = user,
                content = message,
                isSent = false))
        }

//        chatMembersRepository.save(customMapper.chatMembersEntity(conversationId = conversation))
    }
    fun getConversationById(id: Long): ConversationEntity? {
        return conversationRepository.findById(id)
            .orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với id: $id") }
    }
}