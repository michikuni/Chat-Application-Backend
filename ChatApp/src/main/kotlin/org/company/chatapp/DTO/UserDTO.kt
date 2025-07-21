package org.company.chatapp.DTO

import java.time.Instant

data class UserDTO (
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val avatar: String? = null,
    val createAt: Instant? = null,
)