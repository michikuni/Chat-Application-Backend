package org.company.chatapp.service

import org.company.chatapp.DTO.FriendsDTO
import org.company.chatapp.DTO.UserDTO
import org.company.chatapp.entity.UserEntity
import org.springframework.stereotype.Service

@Service
class CustomMapper {
    fun userToDto(entity: UserEntity): UserDTO = UserDTO(
        id = entity.id,
        name = entity.name,
        username = entity.username,
        email = entity.email,
        avatar = entity.avatar
    )
    fun friendshipDto(userEntity: UserEntity, friendshipId: Long): FriendsDTO = FriendsDTO(
        friendshipId = friendshipId,
        id = userEntity.id,
        name = userEntity.name,
        username = userEntity.username,
        email = userEntity.email,
        avatar = userEntity.avatar
    )
}