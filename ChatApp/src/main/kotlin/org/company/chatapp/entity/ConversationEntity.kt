package org.company.chatapp.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "conversation")
data class ConversationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ElementCollection
    @CollectionTable(name = "chat_members", joinColumns = [JoinColumn(name = "conversation_id")])
    @Column(name = "user_id")
    val memberIds: List<Long> = emptyList(),

    @Column(name = "conversation_name")
    val conversationName: String? = null,

    @Column(name = "avatar")
    val avatar: String? = null,

    @Column(name = "number_members")
    val numberMembers: Int? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "last_message_id")
    val lastMessage: Long? = null,
)