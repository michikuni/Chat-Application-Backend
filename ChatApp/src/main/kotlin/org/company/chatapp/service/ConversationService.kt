package org.company.chatapp.service

import org.company.chatapp.DTO.ConversationDTO
import org.company.chatapp.DTO.toEntity
import org.company.chatapp.DTO.toEntityGroup
import org.company.chatapp.entity.ConversationEntity
import org.company.chatapp.repository.ConversationRepository
import org.springframework.stereotype.Service

@Service
class ConversationService(
    private val conversationRepository: ConversationRepository
){
    fun createConversation(conversationDTO: ConversationDTO): ConversationEntity{
        val conversation = conversationDTO.toEntityGroup()
        return conversationRepository.save(conversation)
    }
}