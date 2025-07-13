package org.company.chatapp.repository

import org.company.chatapp.entity.MessageEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Pageable

interface MessageRepository: JpaRepository<MessageEntity, Long> {
    fun findByConversationIdOrderByCreatedAtDesc(
        conversationId: Long,
        pageable: Pageable
    ): List<MessageEntity>
}