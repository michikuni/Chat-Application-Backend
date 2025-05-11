package org.company.chatapp.repository

import org.company.chatapp.DTO.MessageDTO
import org.company.chatapp.entity.MessageEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Pageable

interface MessageRepository: JpaRepository<MessageDTO, Long> {
    fun findByConversationIdOrderByTimestampDesc(
        conversationId: Long,
        pageable: Pageable
    ): List<MessageEntity>
}