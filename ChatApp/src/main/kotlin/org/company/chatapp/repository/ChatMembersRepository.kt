package org.company.chatapp.repository

import org.company.chatapp.entity.ChatMemberEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ChatMembersRepository: JpaRepository<ChatMemberEntity, Long> {

}