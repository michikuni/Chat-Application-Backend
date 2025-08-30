package org.company.chatapp.repository

import org.company.chatapp.DTO.ConversationDTO
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

    @Query("SELECT cs.id, cm1.user_id, ms.sender_id, us.name, us.avatar, ms.content, ms.media_file, ms.created_at, ms.is_sent, cs.theme_color " +
            "FROM conversation cs " +
            "JOIN chat_members cm ON cs.id = cm.conversation_id " +
            "JOIN chat_members cm1 ON cm.conversation_id = cm1.conversation_id " +
            "JOIN user us ON cm1.user_id = us.id " +
            "JOIN message ms ON ms.conversation_id = cs.id " +
            "JOIN (" +
                "SELECT conversation_id, MAX(created_at) AS latest_time " +
                "FROM message " +
                "GROUP BY conversation_id" +
            ") last_msg ON ms.conversation_id = last_msg.conversation_id AND ms.created_at = last_msg.latest_time " +
            "WHERE cm.user_id = :userId AND cm1.user_id != :userId " +
            "ORDER BY ms.created_at DESC;", nativeQuery = true)
    fun findAllConversationByUserId(@Param("userId") userId: Long): List<ConversationDTO>
}