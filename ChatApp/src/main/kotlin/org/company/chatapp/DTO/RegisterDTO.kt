package org.company.chatapp.DTO

import org.company.chatapp.entity.UserEntity
import java.time.Instant

data class RegisterDTO (
    val name:String,
    val username:String,
    val email:String,
    val password:String,
    val avatar:String? = null,
    val createdAt: Instant = Instant.now()
    )


fun RegisterDTO.toEntity(): UserEntity {
    return UserEntity(
        name = this.name,
        username = this.username,
        email = this.email,
        password = this.password,
        avatar = this.avatar,
        createAt = this.createdAt
    )
}
