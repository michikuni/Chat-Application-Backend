package org.company.chatapp.repository

import org.company.chatapp.entity.MessageEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MessageRepository: JpaRepository<MessageEntity, Long> {

}