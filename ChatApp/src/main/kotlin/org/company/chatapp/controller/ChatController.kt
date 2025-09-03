package org.company.chatapp.controller

import org.company.chatapp.DTO.ConversationDTO
import org.company.chatapp.DTO.CreateConversation
import org.company.chatapp.DTO.CreateConversationGroup
import org.company.chatapp.DTO.MessageDTO
import org.company.chatapp.repository.ConversationRepository
import org.company.chatapp.service.ConversationService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.io.File
import java.nio.file.Files
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

    @GetMapping("/allMessage/{conversationId}")
    fun getAllMessage(
        @PathVariable conversationId: Long
    ): ResponseEntity<List<MessageDTO>> {
        val conversation = conversationService.getAllMessage(conversationId = conversationId)
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
        conversationService.createConversation(userId = userId, conversationId = createConversation.conversationId, message = createConversation.message)
        return ResponseEntity.ok("Tạo đoạn chat thành công")
    }

    @PostMapping("/createConversationGroup")
    fun createConversationGroup(
        @RequestPart("data") createConversationGroup: CreateConversationGroup,
        @RequestPart("file") file: MultipartFile
    ):ResponseEntity<Any> {
        try {
            val conversation = conversationService.createGroupConversation(
                members = createConversationGroup.members,
                name = createConversationGroup.name,
                avatar = "")
            val conversationId = conversation.id
            val filename = "${conversationId}_${file.originalFilename}"
            val uploadDir = Paths.get(System.getProperty("user.dir"), "conversation_media/${conversationId}/avatar").toFile()

            if (!uploadDir.exists()) {
                uploadDir.mkdirs()
            }

            val filePath = File(uploadDir, filename)
            file.transferTo(filePath)

            val updatedConversation = conversation.copy(avatar = filename)
            conversationRepository.save(updatedConversation)
            return ResponseEntity.ok(updatedConversation)
        } catch (e: Exception){
            return ResponseEntity.badRequest().build()
        }
    }

    @PostMapping("/updateTheme/{conversationId}")
    fun themeConversation(
        @PathVariable conversationId: Long,
        @RequestBody colors: List<String>
    ): ResponseEntity<Any> {
        conversationService.themeConversation(conversationId = conversationId, colors = colors)
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("/sendMediaFile/{userId}")
    fun sendMediaFile(
        @PathVariable userId: Long,
        @RequestParam conversationId: Long,
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<Any> {
        val filename = "${conversationId}_${userId}_${file.originalFilename}"
        val uploadDir = Paths.get(System.getProperty("user.dir"), "conversation_media/${conversationId}").toFile()

        if (!uploadDir.exists()) {
            uploadDir.mkdirs()
        }

        val filePath = File(uploadDir, filename)

        println("Sending file to ${filePath.path}")
        file.transferTo(filePath)

        conversationService.sendMediaFile(userId = userId, conversationId = conversationId, mediaFile = filename)
        return ResponseEntity.ok("Gửi tin nhắn phương tiện thành công")
    }

    @GetMapping("/getMediaFile/{fileName}")
    fun getMediaFile(
        @PathVariable fileName: String
    ): ResponseEntity<ByteArray> {
        return try {
            val parts = fileName.split("_")
            val conversationId = parts[0]
            val imageFile = Paths.get(System.getProperty("user.dir"), "conversation_media/$conversationId", fileName).toFile()

            if (!imageFile.exists()){
                return ResponseEntity.notFound().build()
            }

            val bytes = imageFile.readBytes()
            val contentType = Files.probeContentType(imageFile.toPath()) ?: "applicaton/octet-stream"

            ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(bytes)
        } catch (e: Exception){
            ResponseEntity.status(500).build()
        }
    }

}