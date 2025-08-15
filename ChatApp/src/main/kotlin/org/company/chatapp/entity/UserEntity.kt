package org.company.chatapp.entity

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name" ,nullable = false)
    val name: String,

    @Column(name = "username" ,nullable = false, unique = true)
    val username: String,

    @Column(name = "email" ,nullable = false, unique = true)
    val email: String,

    @Column(name = "password" ,nullable = false)
    var password: String,

    val role: String = "USER",

    @Column(name = "avatar" ,nullable = true)
    var avatar: String? = null,

    @Column(name = "createAt" ,nullable = false)
    val createAt: Instant = Instant.now(),
)
