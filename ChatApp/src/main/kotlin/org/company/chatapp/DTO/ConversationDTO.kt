package org.company.chatapp.DTO

import java.math.BigInteger
import java.sql.Timestamp
import java.time.LocalDateTime

interface ConversationProjection {
    fun getId(): Long
    fun getGroupAvatar(): String?
    fun getConversationName(): String?
    fun getThemeColor(): String?
    fun getConversationType(): String?
    fun getMembersIds(): String?
    fun getName(): String?
    fun getPairAvatar(): String?
    fun getContent(): String?
    fun getMediaFile(): String?
    fun getSenderId(): Long?
    fun getCreatedAt(): Timestamp?
    fun getIsRead(): Boolean?   // cho SQL boolean
}



data class AllConversationDTO(
    val id: Long,
    val avatar: String?,
    val conversationName: String?,
    val themeColor: String?,
    val conversationType: String?,
    val membersIds: String?,
//    val senderId: Long?,
//    val name: String?,
//    val content: String?,
//    val mediaFile: String?,
//    val createdAt: Timestamp,
//    val isRead: Boolean,
) {
    val membersIdList: List<Long>
        get() = membersIds?.split(",")?.map { it.toLong() } ?: emptyList()
}



data class CreateConversation(
    val conversationId: Long,
    val message: String
)

data class CreateConversationGroup(
    val members: List<Long>,
    val name: String
)

data class GetConversation(
    val id: Long,
    val memberIds: List<Long>,
    val conversationName: String?,
    val avatar: String?,
    val numberMembers: Int,
    val createdAt: Timestamp,
)

enum class ConversationType{
    PAIR,
    GROUP
}