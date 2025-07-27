package org.company.chatapp.entity

import jakarta.persistence.*
import org.company.chatapp.DTO.FriendshipStatus
import java.time.Instant

@Entity
@Table(name = "friends")
data class FriendsEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    val friend: UserEntity,

    @Enumerated(EnumType.STRING)
    val status: FriendshipStatus = FriendshipStatus.PENDING,

    val createdAt: Instant = Instant.now(),
    )