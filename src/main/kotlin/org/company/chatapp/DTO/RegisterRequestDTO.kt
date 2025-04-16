package org.company.chatapp.DTO

import org.company.chatapp.entity.UserEntity

data class RegisterRequestDTO(
    val name:String,
    val username:String,
    val email:String,
    val password:String,
    val avatar:String? = null
)

fun RegisterRequestDTO.toEntity(): UserEntity{
    return UserEntity(
        name = this.name,
        username = this.username,
        email = this.email,
        password = this.password
    )
}