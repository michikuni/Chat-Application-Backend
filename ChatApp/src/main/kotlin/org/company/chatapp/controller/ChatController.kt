package org.company.chatapp.controller

import org.company.chatapp.DTO.createConversation
import org.company.chatapp.repository.UserRepository
import org.company.chatapp.service.ConversationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/chats")
class ChatController (
    private val conversationService: ConversationService,
    private val userRepository: UserRepository
) {
    @PostMapping("/createConversation/{userId}")
    fun createConversation(
        @PathVariable userId: Long,
        @RequestBody createConversation: createConversation
    ): ResponseEntity<Any> {
        conversationService.createConversation(userId = userId, friendId = createConversation.friendId, createConversation.message)
        return ResponseEntity.ok("Tạo đoạn chat thành công")
    }
}