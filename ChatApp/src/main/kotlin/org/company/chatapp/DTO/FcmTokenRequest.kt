package org.company.chatapp.DTO

data class FcmTokenRequest(
    val userId: Long,
    val token: String
)
