package org.company.chatapp.repository

import org.company.chatapp.entity.ConversationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ConversationRepository: JpaRepository<ConversationEntity, Long>{
    @Query("SELECT cu2.user_id FROM chat_members cu1 JOIN chat_members cu2 ON cu1.conversation_id = cu2.conversation_id WHERE cu1.user_id = 5 AND cu2.user_id != 5", nativeQuery = true)
    fun findConversationIdByMemberIds(@Param("userId") userId: Long, @Param("friendId") friendId: Long): Long
}