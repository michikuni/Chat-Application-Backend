package org.company.chatapp.repository

import org.company.chatapp.entity.ConversationParticipantEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ConversationParticipantRepository : JpaRepository<ConversationParticipantEntity, Long> {
    fun findByUserId(userId: Long): List<ConversationParticipantEntity>
}