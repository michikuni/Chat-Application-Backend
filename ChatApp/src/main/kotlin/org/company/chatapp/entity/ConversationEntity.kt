package org.company.chatapp.entity

import com.vladmihalcea.hibernate.type.json.JsonType
import jakarta.persistence.*
import org.company.chatapp.DTO.ConversationType
import org.hibernate.annotations.Type
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
    val numberMembers: Int = 1,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),

    @Type(JsonType::class)
    @Column(columnDefinition = "json")
    val themeColor: List<String>? = null,

    @Enumerated(EnumType.STRING)
    val conversationType: ConversationType = ConversationType.PAIR
)