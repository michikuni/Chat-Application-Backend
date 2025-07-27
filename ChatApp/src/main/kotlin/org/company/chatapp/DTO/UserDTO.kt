package org.company.chatapp.DTO

data class UserDTO (
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val avatar: String? = null
)
