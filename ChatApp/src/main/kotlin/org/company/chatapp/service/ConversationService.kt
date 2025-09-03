package org.company.chatapp.service

import org.company.chatapp.DTO.*
import org.company.chatapp.entity.ConversationEntity
import org.company.chatapp.entity.UserEntity
import org.company.chatapp.repository.*
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import kotlin.reflect.jvm.internal.impl.descriptors.deserialization.PlatformDependentDeclarationFilter.All

@Service
class ConversationService(
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository,
    private val customMapper: CustomMapper,
    private val notificationService: NotificationService
){
    fun getAllMessage(conversationId: Long): List<MessageDTO> {
        val message = messageRepository.findMessageByConversationId(conversationId)
        return message.map { customMapper.messageDto(
            messageEntity = it
        ) }
    }

    fun getAllConversation(userId: Long): List<ConversationProjection> {
        return conversationRepository.findAllConversationByUserId(userId)
    }

//    fun getConversation(userId: Long): List<TestGetCon> {
//        return conversationRepository.findAllMembersByConversationId(userId)
//    }

    fun createConversation(userId: Long, conversationId: Long, message: String){
        val user = userRepository.findById(userId).get()
        val conversation = conversationRepository.findById(conversationId).get()
        messageRepository.save(customMapper.messageEntity(
            createdAt = Instant.now(),
            conversationId = conversation,
            senderId = user,
            content = message,
            mediaFile = null,
            isSent = false))
        conversation.memberIds.map {
            if (userId != it){
                notificationService.sendMessageNotification(
                    userId = it,
                    messages = "Đã gửi một tệp đa phương tiện",
                    friendId = userId)
            }
        }
    }

    fun createGroupConversation(members: List<Long>, name: String, avatar: String): ConversationEntity{
        return conversationRepository.save(customMapper.conversationEntity(
            createdAt = Instant.now(),
            conversationName = name,
            avatar = avatar,
            numberMembers = members.size,
            memberIds = members,
            conversationType = ConversationType.GROUP
        ))
    }

    fun sendMediaFile(userId: Long, conversationId: Long, mediaFile: String){
        val user = userRepository.findById(userId).get()
        val conversation = conversationRepository.findById(conversationId).get()
        messageRepository.save(customMapper.messageEntity(
            createdAt = Instant.now(),
            conversationId = conversation,
            senderId = user,
            content = null,
            mediaFile = mediaFile,
            isSent = false))
        conversation.memberIds.map {
            if (userId != it){
                notificationService.sendMessageNotification(
                    userId = it,
                    messages = "Đã gửi một tệp đa phương tiện",
                    friendId = userId)
            }
        }

    }

    fun themeConversation(conversationId: Long, colors: List<String>){
        val conversation = conversationRepository.findById(conversationId).get()
        val updateTheme = conversation.copy(themeColor = colors)
        conversationRepository.save(updateTheme)
    }

}