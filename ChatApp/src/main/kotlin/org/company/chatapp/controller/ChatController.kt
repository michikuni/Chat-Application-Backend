package org.company.chatapp.controller

import org.company.chatapp.DTO.ConversationDTO
import org.company.chatapp.DTO.CreateConversation
import org.company.chatapp.DTO.MessageDTO
import org.company.chatapp.repository.ConversationRepository
import org.company.chatapp.service.ConversationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.io.File
import java.nio.file.Paths


@RestController
@RequestMapping("/api/chats")
class ChatController(
    private val conversationService: ConversationService,
    private val conversationRepository: ConversationRepository
) {
    @GetMapping("/allConversation/{userId}")
    fun getAllConversation(
        @PathVariable userId: Long
    ): ResponseEntity<List<ConversationDTO>> {
        val conversation = conversationService.getAllConversation(userId)
        return ResponseEntity.ok(conversation)
    }

    @GetMapping("/allMessage/{userId}")
    fun getAllMessage(
        @PathVariable userId: Long,
        @RequestParam friendId: Long
    ): ResponseEntity<List<MessageDTO>> {
        val conversation = conversationService.getAllMessage(userId, friendId)
        return try {
            ResponseEntity.ok(conversation)
        } catch (e: ResponseStatusException){
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/createConversation/{userId}")
    fun createConversation(
        @PathVariable userId: Long,
        @RequestBody createConversation: CreateConversation
    ): ResponseEntity<Any> {
        conversationService.createConversation(userId = userId, friendId = createConversation.friendId, message = createConversation.message)
        return ResponseEntity.ok("Tạo đoạn chat thành công")
    }

    @PostMapping("/sendMediaFile/{userId}")
    fun sendMediaFile(
        @PathVariable userId: Long,
        @RequestParam friendId: Long,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<Any> {
        val conversation = conversationRepository.findConversationBetweenUsers(userId = userId, friendId = friendId)
        val filename = "${conversation}_${userId}_${file.originalFilename}"
        val uploadDir = Paths.get(System.getProperty("user.dir"), conversation.toString()).toFile()

        if (!uploadDir.exists()) {
            uploadDir.mkdirs()
        }

        val filePath = File(uploadDir, filename)

        println("Sending file to ${filePath.path}")
        file.transferTo(filePath)

        conversationService.sendMediaFile(userId = userId, friendId = friendId, mediaFile = filename)
        return ResponseEntity.ok("Gửi tin nhắn phương tiện thành công")
    }

}