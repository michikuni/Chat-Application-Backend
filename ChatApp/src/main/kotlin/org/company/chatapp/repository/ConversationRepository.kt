package org.company.chatapp.repository

import org.company.chatapp.entity.ConversationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ConversationRepository: JpaRepository<ConversationEntity, Long>