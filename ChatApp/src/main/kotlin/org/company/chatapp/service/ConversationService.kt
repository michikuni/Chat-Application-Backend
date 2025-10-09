package org.company.chatapp.service

import org.company.chatapp.DTO.*
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
) {

    fun getAllMessage(conversationId: Long): List<MessageDTO> {
        val conversation = conversationRepository.findById(conversationId)
            .orElseThrow {
                ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy cuộc trò chuyện với ID $conversationId")
            }

        val messages = messageRepository.findMessageByConversationId(conversation.id!!)
        return messages.map { customMapper.messageDto(it) }
    }

    fun getAllConversation(userId: Long): List<ConversationDTO> {
        if (!userRepository.existsById(userId)) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng với ID $userId không tồn tại")
        }
        return conversationRepository.findAllConversationByUserId(userId)
    }

    fun findConversation(userId: Long, friendId: Long): Long {
        val userExists = userRepository.existsById(userId)
        val friendExists = userRepository.existsById(friendId)
        if (!userExists || !friendExists) {
            throw ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng hoặc bạn bè không tồn tại")
        }

        return conversationRepository.findConversationBetweenUsers(userId, friendId)
            ?: createNewConversation(userId, friendId)
    }

    private fun createNewConversation(userId: Long, friendId: Long): Long {
        val conversation = customMapper.conversationEntity(
            avatar = null,
            createdAt = Instant.now(),
            conversationType = ConversationType.PAIR,
            numberMembers = 2,
            memberIds = listOf(userId, friendId),
            conversationName = null,
        )
        return conversationRepository.save(conversation).id
    }

    fun createMessage(userId: Long, conversationId: Long, message: String) {
        val user = userRepository.findById(userId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng với ID $userId không tồn tại") }

        val conversation = conversationRepository.findById(conversationId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy cuộc trò chuyện $conversationId") }

        val titleGroupMessage = if (conversation.conversationType == ConversationType.GROUP)
            conversation.conversationName else null

        messageRepository.save(
            customMapper.messageEntity(
                createdAt = Instant.now(),
                conversationId = conversation,
                senderId = user,
                content = message,
                mediaFile = null,
                isSent = false
            )
        )

        conversation.memberIds.forEach {
            if (it != userId) {
                notificationService.sendMessageNotification(
                    userId = it,
                    messages = message,
                    friendId = userId,
                    titleGroup = titleGroupMessage
                )
            }
        }
    }

    fun createGroupConversation(members: List<Long>, name: String, avatar: String?): ConversationEntity {
        if (members.size < 2)
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Nhóm phải có ít nhất 2 thành viên")

        if (name.isBlank())
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên nhóm không được để trống")

        val conversation = customMapper.conversationEntity(
            createdAt = Instant.now(),
            conversationName = name,
            avatar = avatar,
            numberMembers = members.size,
            memberIds = members,
            conversationType = ConversationType.GROUP
        )
        return conversationRepository.save(conversation)
    }

    fun sendMediaFile(userId: Long, conversationId: Long, mediaFile: String) {
        val user = userRepository.findById(userId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy người dùng $userId") }

        val conversation = conversationRepository.findById(conversationId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy cuộc trò chuyện $conversationId") }

        val titleGroupMessage = if (conversation.conversationType == ConversationType.GROUP) {
            conversation.conversationName ?: "Nhóm của bạn"
        } else null

        messageRepository.save(
            customMapper.messageEntity(
                createdAt = Instant.now(),
                conversationId = conversation,
                senderId = user,
                content = null,
                mediaFile = mediaFile,
                isSent = false
            )
        )

        conversation.memberIds.forEach {
            if (it != userId) {
                notificationService.sendMessageNotification(
                    userId = it,
                    messages = "Đã gửi một tệp đa phương tiện",
                    friendId = userId,
                    titleGroup = titleGroupMessage
                )
            }
        }
    }

    fun themeConversation(conversationId: Long, colors: List<String>) {
        val conversation = conversationRepository.findById(conversationId)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy cuộc trò chuyện $conversationId") }

        if (colors.isEmpty()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Danh sách màu không được để trống")
        }

        val updatedConversation = conversation.copy(themeColor = colors)
        conversationRepository.save(updatedConversation)
    }
}
