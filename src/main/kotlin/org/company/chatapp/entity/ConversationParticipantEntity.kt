package org.company.chatapp.entity

import jakarta.persistence.*

@Entity
@Table(name = "conversation_participants")
data class ConversationParticipantEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne @JoinColumn(name = "conversation_id")
    val conversation: ConversationEntity,

    @ManyToOne @JoinColumn(name = "user_id")
    val user: UserEntity
)
