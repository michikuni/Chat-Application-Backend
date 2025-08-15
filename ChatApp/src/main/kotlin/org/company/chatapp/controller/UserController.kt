package org.company.chatapp.controller

import org.company.chatapp.DTO.UserDTO
import org.company.chatapp.service.UserService
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@RestController
@RequestMapping("/api/users")
class UserController (
    private val userService: UserService
){
    private val uploadDir = "avatars"

    init {
        val dir = File(uploadDir)
        if (!dir.exists()) dir.mkdirs()
    }

    @GetMapping("/test")
    fun test(): String = "OK"

    @PostMapping("/upload_avatar/{userId}")
    fun uploadAvatar(@RequestParam("file") file: MultipartFile, @PathVariable userId: Long): ResponseEntity<String> {
        val fileName = userId.toString() + "_" + file.originalFilename
        val filePath = "$uploadDir/$fileName"
        file.transferTo(File(filePath))
        userService.updateAvatar(userId = userId, avatarUrl = filePath)
        return try {
            ResponseEntity.ok("Cập nhật avatar thành công")
        } catch (e: RuntimeException){
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @GetMapping("/get_avatar/{userId}")
    fun getAvatar(@PathVariable userId: Long): ResponseEntity<ByteArray> {
        val avtarPath = userService.getAvatar(userId)
        val path = avtarPath?.let { Paths.get(it) }
        return if (path != null){
            val imageByte = Files.readAllBytes(path)
            val contentType = Files.probeContentType(path) ?: MediaType.APPLICATION_OCTET_STREAM_VALUE
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .body(imageByte)
        } else {
            ResponseEntity.badRequest().body(ByteArray(0))
        }

    }

    @GetMapping("/all_users/{userId}")
    fun allUsers(
        @PathVariable userId: Long
    ): ResponseEntity<List<UserDTO>>{
        return ResponseEntity.ok(userService.getAllFriendsById(userId))
    }
}