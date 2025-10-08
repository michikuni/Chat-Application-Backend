package org.company.chatapp.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "feed_reactions")
data class FeedReactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "feed_id", nullable = false)
    val feedId: Long,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "reaction_type")
    val reactionType: Boolean? = false,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now()
)
