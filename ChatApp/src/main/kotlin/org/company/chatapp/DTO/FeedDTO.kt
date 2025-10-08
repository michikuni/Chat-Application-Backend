package org.company.chatapp.DTO

import org.company.chatapp.entity.*
import java.time.Instant

data class FeedDTO (
    val id: Long,
    val posterId: Long,
    val content: String?,
    val createdAt: Instant,
    val mediaFile: String?,
)

data class FeedCommentDTO(
    val id: Long,
    val feed: FeedEntity,
    val userId: Long,
    val content: String,
    val createdAt: Instant,
    )

data class FeedShareDTO(
    val id: Long,
    val feed: FeedEntity,

    val userId: Long,
    val createdAt: Instant,
)

data class FeedReactionDTO (
    val id: Long,
    val feed: FeedEntity,

    val userId: Long,
    val reactions: Boolean,
    val createdAt: Instant
)