package org.company.chatapp.controller

import org.company.chatapp.DTO.*
import org.company.chatapp.repository.ConversationRepository
import org.company.chatapp.service.ConversationService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths


@RestController
@RequestMapping("/api/chats")
class ChatController(
    private val conversationService: ConversationService,
    private val conversationRepository: ConversationRepository
) {
//    @GetMapping("/test/{userId}")
//    fun test(
//        @PathVariable("userId") userId: Long,
//    ){
//        conversationService.debugTypes(userId)
//    }
    @GetMapping("/allConversation/{userId}")
    fun getAllConversation(
        @PathVariable userId: Long
    ): ResponseEntity<List<ConversationDTO>> {
        val conversation = conversationService.getAllConversation(userId)
        return ResponseEntity.ok(conversation)
    }

    @GetMapping("/allMessage/{conversationId}")
    fun getAllMessage(
        @PathVariable conversationId: Long
    ): ResponseEntity<List<MessageDTO>> {
        val messages = conversationService.getAllMessage(conversationId)
        return ResponseEntity.ok(messages)
    }

    @PostMapping("/findConversation/{userId}/{friendId}")
    fun findConversation(
        @PathVariable userId: Long,
        @PathVariable friendId: Long
    ): ResponseEntity<Map<String, Any>> {
        val conversationId = conversationService.findConversation(userId, friendId)
        return ResponseEntity.ok(mapOf("conversationId" to conversationId))
    }

    @PostMapping("/createMessage/{userId}")
    fun createMessage(
        @PathVariable userId: Long,
        @RequestBody createConversation: CreateConversation
    ): ResponseEntity<Map<String, String>> {
        conversationService.createMessage(
            userId = userId,
            conversationId = createConversation.conversationId,
            message = createConversation.message
        )
        return ResponseEntity.ok(mapOf("message" to "Tạo đoạn chat thành công"))
    }

    @PostMapping("/createConversationGroup")
    fun createConversationGroup(
        @RequestPart("data") createConversationGroup: CreateConversationGroup,
        @RequestPart("file") file: MultipartFile
    ): ResponseEntity<Any> {
        val conversation = conversationService.createGroupConversation(
            members = createConversationGroup.members,
            name = createConversationGroup.name,
            avatar = ""
        )

        val conversationId = conversation.id
        val filename = "${conversationId}_${file.originalFilename}"
        val uploadDir = Paths.get(System.getProperty("user.dir"), "conversation_media/$conversationId/avatar").toFile()
        if (!uploadDir.exists()) uploadDir.mkdirs()

        val filePath = File(uploadDir, filename)
        file.transferTo(filePath)

        val updatedConversation = conversation.copy(avatar = filename)
        conversationRepository.save(updatedConversation)

        return ResponseEntity.ok(mapOf(
            "message" to "Tạo nhóm thành công",
            "conversationId" to updatedConversation.id,
            "avatar" to updatedConversation.avatar
        ))
    }

    @GetMapping("/getConversationAvatar/{fileName}")
    fun getConversationAvatar(
        @PathVariable fileName: String
    ): ResponseEntity<ByteArray> {
        val parts = fileName.split("_")
        val conversationId = parts[0]
        val imageFile = Paths.get(System.getProperty("user.dir"), "conversation_media/$conversationId/avatar", fileName).toFile()

        if (!imageFile.exists()) {
            return ResponseEntity.notFound().build()
        }

        val bytes = imageFile.readBytes()
        val contentType = Files.probeContentType(imageFile.toPath()) ?: "application/octet-stream"
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body(bytes)
    }

    @PostMapping("/updateTheme/{conversationId}")
    fun updateTheme(
        @PathVariable conversationId: Long,
        @RequestBody colors: List<String>
    ): ResponseEntity<Map<String, String>> {
        conversationService.themeConversation(conversationId, colors)
        return ResponseEntity.ok(mapOf("message" to "Cập nhật chủ đề thành công"))
    }

    @PostMapping("/sendMediaFile/{userId}")
    fun sendMediaFile(
        @PathVariable userId: Long,
        @RequestParam conversationId: Long,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<Map<String, String>> {
        val filename = "${conversationId}_${userId}_${file.originalFilename}"
        val uploadDir = Paths.get(System.getProperty("user.dir"), "conversation_media/$conversationId").toFile()
        if (!uploadDir.exists()) uploadDir.mkdirs()

        val filePath = File(uploadDir, filename)
        file.transferTo(filePath)

        conversationService.sendMediaFile(userId, conversationId, filename)
        return ResponseEntity.ok(mapOf("message" to "Gửi tin nhắn phương tiện thành công"))
    }

    @GetMapping("/getMediaFile/{fileName}")
    fun getMediaFile(
        @PathVariable fileName: String
    ): ResponseEntity<ByteArray> {
        val parts = fileName.split("_")
        val conversationId = parts[0]
        val mediaFile = Paths.get(System.getProperty("user.dir"), "conversation_media/$conversationId", fileName).toFile()

        if (!mediaFile.exists()) {
            return ResponseEntity.notFound().build()
        }

        val bytes = mediaFile.readBytes()
        val contentType = Files.probeContentType(mediaFile.toPath()) ?: "application/octet-stream"
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .body(bytes)
    }

}