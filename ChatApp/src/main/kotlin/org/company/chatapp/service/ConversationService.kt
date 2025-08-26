package org.company.chatapp.service

import org.company.chatapp.DTO.ConversationDTO
import org.company.chatapp.DTO.MessageDTO
import org.company.chatapp.entity.ConversationEntity
import org.company.chatapp.entity.UserEntity
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
    fun getAllMessage(userId: Long, friendId: Long): List<MessageDTO> {
        val conversationId = conversationRepository.findConversationBetweenUsers(userId, friendId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy id hội thoại")
        val message = messageRepository.findMessageByConversationId(conversationId)
        return message.map { customMapper.messageDto(
            messageEntity = it
        ) }
    }

    fun getAllConversation(userId: Long): List<ConversationDTO> {
        return conversationRepository.findAllConversationByUserId(userId)
    }

    fun createConversation(userId: Long, friendId: Long, message: String){
        val (user, conversation) = getOrCreateConversation(userId, friendId)
        messageRepository.save(customMapper.messageEntity(
            createdAt = Instant.now(),
            conversationId = conversation,
            senderId = user,
            content = message,
            mediaFile = null,
            isSent = false))
        notificationService.sendMessageNotification(
            userId = friendId,
            messages = message,
            friendId = userId)
    }
    fun sendMediaFile(userId: Long, friendId: Long, mediaFile: String){
        val (user, conversation) = getOrCreateConversation(userId, friendId)
        messageRepository.save(customMapper.messageEntity(
            createdAt = Instant.now(),
            conversationId = conversation,
            senderId = user,
            content = null,
            mediaFile = mediaFile,
            isSent = false))
        notificationService.sendMessageNotification(
            userId = friendId,
            messages = "Đã gửi một tệp đa phương tiện",
            friendId = userId)
    }

    private fun getOrCreateConversation(userId: Long, friendId: Long): Pair<UserEntity, ConversationEntity> {
        val user = userRepository.findById(userId).get()
        val friend = userRepository.findById(friendId).get()
        val conversationId = conversationRepository.findConversationBetweenUsers(userId, friendId)

        val conversation = if (conversationId == null) {
            conversationRepository.save(customMapper.conversationEntity(
                avatar = friend.avatar,
                numberMembers = 2,
                conversationName = friend.name,
                memberIds = listOf(user.id, friend.id),
                createdAt = Instant.now()
            ))
        } else {
            conversationRepository.findById(conversationId).get()
        }

        return user to conversation
    }

    fun getConversationMedia(conversationId: Long): List<MessageDTO>{
        val messages = messageRepository.findMessageByConversationId(conversationId)
            .filter { it.mediaFile != null }
            .map { customMapper.messageDto(it) }
        return messages
    }

}