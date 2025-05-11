package org.company.chatapp.service

import org.company.chatapp.DTO.ConversationDTO
import org.company.chatapp.DTO.MessageDTO
import org.company.chatapp.DTO.toConversationDTO
import org.company.chatapp.DTO.toMessageDTO
import org.company.chatapp.repository.ConversationParticipantRepository
import org.company.chatapp.repository.ConversationRepository
import org.company.chatapp.repository.MessageRepository
import org.springframework.data.domain.*
import org.springframework.stereotype.Service

@Service
class ChatService(
    private val conversationParticipantRepository: ConversationParticipantRepository,
    private val conversationRepository: ConversationRepository,
    private val messageRepository: MessageRepository
){
    fun getUserConversations(userId: Long): List<ConversationDTO> {
        val participants = conversationParticipantRepository.findByUserId(userId)

        return participants.map { participant ->
            val conversation = participant.conversation
            val unreadCount = messageRepository
                .findByConversationIdOrderByTimestampDesc(conversation.id, Pageable.unpaged())
                .count { !it.isRead && it.sender.id != userId }

            toConversationDTO(conversation, unreadCount)
        }
    }

    fun getMessages(chatId: Long, page: Int, size: Int): List<MessageDTO> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"))
        return messageRepository
            .findByConversationIdOrderByTimestampDesc(chatId, pageable)
            .map { toMessageDTO(it) }
    }
}
