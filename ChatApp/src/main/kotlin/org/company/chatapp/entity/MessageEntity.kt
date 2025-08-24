package org.company.chatapp.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "message")
data class MessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    val conversationId: ConversationEntity,

    @ManyToOne
    @JoinColumn(name = "sender_id")
    val senderId: UserEntity,

    @Column(name = "content")
    val content: String? = null,

    @Column(name = "media_file")
    val mediaFile: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Column(name = "is_sent", nullable = false)
    val isRead: Boolean = false
)
