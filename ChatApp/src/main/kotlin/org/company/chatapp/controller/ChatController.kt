package org.company.chatapp.controller

import org.company.chatapp.DTO.ConversationDTO
import org.company.chatapp.DTO.UserDTO
import org.company.chatapp.repository.ConversationRepository
import org.company.chatapp.service.ConversationService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.User
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/chats")
class ChatController (
    private val conversationService: ConversationService
) {
    @PostMapping("/createConversation/{userId}")
    fun createConversation(
        @PathVariable userId: UserDTO,
        @RequestBody friendId: UserDTO,
        @RequestBody message: String
    ): ResponseEntity<Any> {
        conversationService.createConversation(userId = userId, friendId = friendId, message = message)
        return ResponseEntity.ok("Tạo đoạn chat thành công")
    }
}