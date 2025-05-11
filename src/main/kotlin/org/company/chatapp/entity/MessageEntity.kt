package org.company.chatapp.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "messages")
data class MessageEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne @JoinColumn(name = "conversation_id")
    val conversation: ConversationEntity,

    @ManyToOne @JoinColumn(name = "sender_id")
    val sender: UserEntity,

    val text: String,
    val timestamp: Instant = Instant.now(),
    val isRead: Boolean = false
)
