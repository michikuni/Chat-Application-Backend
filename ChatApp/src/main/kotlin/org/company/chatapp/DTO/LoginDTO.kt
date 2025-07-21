package org.company.chatapp.DTO


data class LoginDTO(
    val username: String,
    val password: String
)

data class LoginResponseDTO(
    val username: String,
    val token: String? = null
)
