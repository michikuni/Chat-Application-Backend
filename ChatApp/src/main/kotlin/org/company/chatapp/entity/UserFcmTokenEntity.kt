package org.company.chatapp.entity

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "user_fcm_token")
data class UserFcmTokenEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id")
    val userId: Long,

    val token: String,

    @Column(name = "created_at")
    val createdAt: Instant,
 )