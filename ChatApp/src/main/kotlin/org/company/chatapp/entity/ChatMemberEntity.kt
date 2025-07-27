package org.company.chatapp.entity

import jakarta.persistence.*

@Entity
@Table(name = "chat_members")
data class ChatMemberEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    val conversation: ConversationEntity? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val userId: UserEntity? = null
)