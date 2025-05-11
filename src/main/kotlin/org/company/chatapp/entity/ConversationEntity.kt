package org.company.chatapp.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "conversation")
data class ConversationEntity (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    val type: String,
    val name: String?,
    val avatar: String?,

    @Column(name = "updated_at")
    val updatedAt: Instant = Instant.now(),

    @OneToOne
    @JoinColumn(name = "last_message_id")
    val lastMessage: MessageEntity? = null
)