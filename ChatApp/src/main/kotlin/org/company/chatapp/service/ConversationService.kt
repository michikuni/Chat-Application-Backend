package org.company.chatapp.service

import org.company.chatapp.DTO.ConversationDTO
import org.company.chatapp.DTO.MessageDTO
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
    private val userRepository: UserRepository,
    private val customMapper: CustomMapper,
    private val notificationService: NotificationService
){
    fun getAllMessage(userId: Long, friendId: Long): List<MessageDTO>? {
        val conversationId = conversationRepository.findConversationBetweenUsers(userId, friendId)
        val message = conversationId?.let { messageRepository.findMessageByConversationId(it) }
        return message?.map { ms ->
            customMapper.messageDto(messageEntity = ms)
        }
    }

    fun getAllConversation(userId: Long): List<ConversationDTO>? {
        return conversationRepository.findAllConversationByUserId(userId)
    }

    fun createConversation(userId: Long, friendId: Long, message: String){
        val user = userRepository.findById(userId).get()
        val friend = userRepository.findById(friendId).get()
        val conversationId = conversationRepository.findConversationBetweenUsers(userId, friendId)
        var conversation: ConversationEntity? = null

        if(conversationId == null){
            conversation = conversationRepository.save(customMapper.conversationEntity(
                avatar = friend.avatar, numberMembers = 2,
                conversationName = friend.name,
                memberIds = listOf(user.id, friend.id), createdAt = Instant.now()))
        } else {
            conversation = conversationRepository.findById(conversationId).get()
        }
        messageRepository.save(customMapper.messageEntity(
            createdAt = Instant.now(),
            conversationId = conversation,
            senderId = user,
            content = message,
            isSent = false))
        notificationService.sendMessageNotification(userId = friendId, messages = message, friendId = friendId)
    }
    fun getConversationById(id: Long): ConversationEntity? {
        return conversationRepository.findById(id)
            .orElseThrow { throw ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng với id: $id") }
    }
}