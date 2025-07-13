package org.company.chatapp.DTO


data class LoginDTO(
    val account: String,
    val password: String,
)

data class LoginResponseDTO(
    val userId: Long,
    val account: String,
    val token: String? = null,
)
