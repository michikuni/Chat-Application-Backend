package org.company.chatapp.controller

import org.company.chatapp.DTO.ConversationDTO
import org.company.chatapp.DTO.CreateConversation
import org.company.chatapp.DTO.MessageDTO
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

    @GetMapping("/allMessage/{userId}")
    fun getAllMessage(
        @PathVariable userId: Long,
        @RequestParam friendId: Long
    ): ResponseEntity<List<MessageDTO>> {
        val conversation = conversationService.getAllMessage(userId, friendId)
        return if (conversation != null) {
            ResponseEntity.ok(conversation)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/allConversation/{userId}")
    fun getAllConversation(
        @PathVariable userId: Long
    ): ResponseEntity<List<ConversationDTO>> {
        val conversation = conversationService.getAllConversation(userId)
        return if (conversation != null) {
            ResponseEntity.ok(conversation)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}