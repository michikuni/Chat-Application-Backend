package org.company.chatapp.repository

import org.company.chatapp.DTO.MessageDTO
import org.company.chatapp.entity.MessageEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MessageRepository: JpaRepository<MessageEntity, Long> {
    @Query(value = "SELECT * FROM message WHERE conversation_id = :conversationId", nativeQuery = true)
    fun findMessageByConversationId(@Param("conversationId") conversationId: Long): List<MessageEntity>
}