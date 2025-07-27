package org.company.chatapp.DTO

import org.company.chatapp.entity.UserEntity

data class UserDTO (
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val avatar: String? = null
)
