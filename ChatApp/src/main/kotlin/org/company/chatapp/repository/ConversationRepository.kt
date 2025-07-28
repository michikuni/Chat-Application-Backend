package org.company.chatapp.repository

import org.company.chatapp.entity.ConversationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ConversationRepository: JpaRepository<ConversationEntity, Long>{
    @Query(
        """
    SELECT conversation_id
    FROM chat_members
    GROUP BY conversation_id
    HAVING 
        COUNT(*) = 2 AND
        SUM(CASE WHEN user_id = :userId THEN 1 ELSE 0 END) = 1 AND
        SUM(CASE WHEN user_id = :friendId THEN 1 ELSE 0 END) = 1
    """,
        nativeQuery = true
    )
    fun findConversationBetweenUsers(@Param("userId") userId: Long, @Param("friendId") friendId: Long): Long?

}