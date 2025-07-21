package org.company.chatapp.service

import org.company.chatapp.DTO.UserDTO
import org.company.chatapp.entity.UserEntity
import org.springframework.stereotype.Service

@Service
class CustomMapper {
    fun toDto(entity: UserEntity): UserDTO = UserDTO(
        id = entity.id,
        name = entity.name,
        username = entity.username,
        email = entity.email,
        avatar = entity.avatar,
        createAt = entity.createAt,
    )
}