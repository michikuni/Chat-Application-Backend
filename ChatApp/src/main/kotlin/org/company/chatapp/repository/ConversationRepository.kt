package org.company.chatapp.repository

import org.company.chatapp.DTO.ConversationDTO
import org.company.chatapp.entity.ConversationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ConversationRepository: JpaRepository<ConversationEntity, Long>{
    @Query(
        """
    SELECT cm.conversation_id
    FROM chat_members cm
    JOIN conversation cs ON cs.id = cm.conversation_id
    WHERE cs.conversation_type = 'PAIR'
    GROUP BY cm.conversation_id
    HAVING 
        SUM(CASE WHEN cm.user_id = :userId THEN 1 ELSE 0 END) = 1
        AND SUM(CASE WHEN cm.user_id = :friendId THEN 1 ELSE 0 END) = 1
    """,
        nativeQuery = true
    )
    fun findConversationBetweenUsers(@Param("userId") userId: Long, @Param("friendId") friendId: Long): Long?

    @Query(value = """
    SELECT 
        cs.id AS id,
        cs.avatar AS groupAvatar,
        cs.conversation_name AS conversationName,
        cs.theme_color AS themeColor,
        cs.conversation_type AS conversationType,
        GROUP_CONCAT(DISTINCT cm.user_id) AS membersIds,
        GROUP_CONCAT(DISTINCT u.name) AS name,
        GROUP_CONCAT(DISTINCT u.avatar) AS pairAvatar,
        ms.content AS content,
        ms.media_file AS mediaFile,
        ms.sender_id AS senderId,
        ms.created_at AS createdAt,
        cs.created_at AS csCreatedAt,
        ms.is_sent AS isRead
    FROM conversation cs
    JOIN chat_members cm ON cs.id = cm.conversation_id
    JOIN user u ON cm.user_id = u.id
    LEFT JOIN (
        SELECT m1.*
        FROM message m1
        JOIN (
            SELECT conversation_id, MAX(created_at) AS maxCreatedAt
            FROM message
            GROUP BY conversation_id
        ) m2 
          ON m1.conversation_id = m2.conversation_id 
         AND m1.created_at = m2.maxCreatedAt
    ) ms ON cs.id = ms.conversation_id
    WHERE cs.id IN (
        SELECT conversation_id 
        FROM chat_members 
        WHERE user_id = :userId
    )
    GROUP BY 
        cs.id, cs.avatar, cs.conversation_name, 
        cs.theme_color, cs.conversation_type,
        ms.id, ms.content, ms.media_file, ms.sender_id, ms.created_at 
    ORDER BY COALESCE(ms.created_at, cs.created_at) DESC;
""", nativeQuery = true)
    fun findAllConversationByUserId(@Param("userId") userId: Long): List<ConversationDTO>


    @Query(value = "SELECT \n" +
            "    cs.id AS id,\n" +
            "    cs.avatar AS groupAvatar,\n" +
            "    cs.conversation_name AS conversationName,\n" +
            "    cs.theme_color AS themeColor,\n" +
            "    cs.conversation_type AS conversationType,\n" +
            "    GROUP_CONCAT(DISTINCT cm.user_id) AS membersIds,\n" +
            "    GROUP_CONCAT(DISTINCT u.name) AS name,\n" +
            "    GROUP_CONCAT(DISTINCT u.avatar) AS pairAvatar,\n" +
            "    ms.content AS content,\n" +
            "    ms.media_file AS mediaFile,\n" +
            "    ms.sender_id AS senderId,\n" +
            "    ms.created_at AS createdAt, \n" +
            "    ms.is_sent AS isRead \n" +
            "FROM conversation cs\n" +
            "JOIN chat_members cm ON cs.id = cm.conversation_id\n" +
            "JOIN user u ON cm.user_id = u.id\n" +
            "LEFT JOIN (\n" +
            "    SELECT m1.*\n" +
            "    FROM message m1\n" +
            "    JOIN (\n" +
            "        SELECT conversation_id, MAX(created_at) AS maxCreatedAt\n" +
            "        FROM message\n" +
            "        GROUP BY conversation_id\n" +
            "    ) m2 \n" +
            "      ON m1.conversation_id = m2.conversation_id \n" +
            "     AND m1.created_at = m2.maxCreatedAt\n" +
            ") ms ON cs.id = ms.conversation_id\n" +
            "WHERE cs.id IN (\n" +
            "    SELECT conversation_id \n" +
            "    FROM chat_members \n" +
            "    WHERE user_id = :userId\n" +
            ")\n" +
            "GROUP BY \n" +
            "    cs.id, cs.avatar, cs.conversation_name, \n" +
            "    cs.theme_color, cs.conversation_type,\n" +
            "    ms.id, ms.content, ms.media_file, ms.sender_id, ms.created_at;",
        nativeQuery = true)
    fun debugFindRawNative(@Param("userId") userId: Long): List<Array<Any?>>

}