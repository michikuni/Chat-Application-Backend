package org.company.chatapp.controller

import org.company.chatapp.DTO.ConversationDTO
import org.company.chatapp.DTO.CreateConversation
import org.company.chatapp.service.ConversationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/chats")
class ChatController (
    private val conversationService: ConversationService
) {
    @PostMapping("/createConversation/{userId}")
    fun createConversation(
        @PathVariable userId: Long,
        @RequestBody createConversation: CreateConversation
    ): ResponseEntity<Any> {
        conversationService.createConversation(userId = userId, friendId = createConversation.friendId, createConversation.message)
        return ResponseEntity.ok("Tạo đoạn chat thành công")
    }

    @GetMapping("/allConversations/{userId}")
    fun getAllConversation(
        @PathVariable userId: Long,
        @RequestParam friendId: Long
    ): ResponseEntity<List<ConversationDTO>> {
        val conversation = conversationService.getConversation(userId, friendId)
        return if (conversation != null) {
            ResponseEntity.ok(conversation)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}