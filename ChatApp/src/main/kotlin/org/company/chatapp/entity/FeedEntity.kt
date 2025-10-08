package org.company.chatapp.entity

import jakarta.persistence.*
import org.company.chatapp.DTO.UserDTO
import java.time.Instant

@Entity
@Table(name = "feeds")
data class FeedEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "poster_id", nullable = false)
    val posterId: Long,

    @Column(name = "content", columnDefinition = "TEXT")
    val content: String? = null,

    @Column(name = "media_file")
    val mediaFile: String? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: Instant = Instant.now(),
)
