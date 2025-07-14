package org.company.chatapp.controller

import org.company.chatapp.DTO.ConversationDTO
import org.company.chatapp.service.ConversationService
import org.company.chatapp.service.MessageService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/chats")
class ChatController (
    private val messageService: MessageService,
    private val conversationService: ConversationService,
) {
    @PostMapping("/create_conversation")
    fun createConversation(
        @RequestBody conversation: ConversationDTO,
    ): ResponseEntity<Any> {
        conversationService.createConversation(conversation)
        return ResponseEntity.ok("Tạo đoạn chat thành công")
    }
//    @GetMapping
//    fun getAllConversationsForUser(
//        @RequestParam userId: Long
//    ): ResponseEntity<List<ConversationDTO>> {
//        val conversations = chatService.getUserConversations(userId)
//        return ResponseEntity.ok(conversations)
//    }
//
//    @GetMapping("/{chatId}/messages")
//    fun getMessages(
//        @PathVariable chatId: Long,
//        @RequestParam(defaultValue = "0") page: Int,
//        @RequestParam(defaultValue = "20") size: Int
//    ): ResponseEntity<List<MessageDTO>> {
//        val messages = chatService.getMessages(chatId, page, size)
//        return ResponseEntity.ok(messages)
//    }
}