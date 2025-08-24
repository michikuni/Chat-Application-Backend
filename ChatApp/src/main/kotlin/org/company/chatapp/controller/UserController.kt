package org.company.chatapp.controller

import org.company.chatapp.DTO.UserDTO
import org.company.chatapp.filter.CustomUserDetails
import org.company.chatapp.service.UserService
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
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

    @PostMapping("/upload_avatar")
    fun uploadAvatar(
        @RequestParam("file") file: MultipartFile,
        authentication: Authentication
    ): ResponseEntity<String> {
        return try {
            val userDetails = authentication.principal as CustomUserDetails
            val userId = userDetails.getId()
            println("USER ID: $userId")

            val fileName = "${userId}_${file.originalFilename}"

            // Lấy project root (nơi app đang chạy) + folder "avatars"
            val uploadDir = Paths.get(System.getProperty("user.dir"), "avatars").toFile()

            // Tạo thư mục nếu chưa tồn tại
            if (!uploadDir.exists()) {
                uploadDir.mkdirs()
            }

            val filePath = File(uploadDir, fileName)

            println("PATH: ${filePath.absolutePath}")
            file.transferTo(filePath)

            // chỉ lưu tên file vào DB
            userService.updateAvatar(userId, fileName)

            ResponseEntity.ok("Cập nhật avatar thành công")
        } catch (e: ResponseStatusException) {
            e.printStackTrace()
            ResponseEntity.badRequest().body("Lỗi khi upload avatar: ${e.message}")
        }
    }

    @GetMapping("/get_user_info/{userId}")
    fun getUserInfo(
        @PathVariable userId: Long
    ): ResponseEntity<UserDTO> {
        val response = userService.getUserInfo(userId = userId)
        return try {
            ResponseEntity.ok(response)
        } catch (e: ResponseStatusException){
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/get_avatar/{userId}")
    fun getAvatar(
        @PathVariable userId: Long
    ): ResponseEntity<Any> {
        return try {
            val user = userService.getUserById(userId)
                ?: return ResponseEntity.notFound().build()

            val avatarFileName = user.avatar
                ?: return ResponseEntity.notFound().build()

            val avatarFile = Paths.get(System.getProperty("user.dir"), "avatars", avatarFileName).toFile()

            if (!avatarFile.exists()) {
                return ResponseEntity.notFound().build()
            }

            val bytes = avatarFile.readBytes()

            // đoán content-type theo đuôi file
            val contentType = Files.probeContentType(avatarFile.toPath()) ?: "application/octet-stream"

            ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(bytes)
        } catch (e: Exception) {
            println("Người dùng với ID: $userId không có avatar")
            ResponseEntity.status(500).build()
        }
    }


    @GetMapping("/all_users/{userId}")
    fun allUsers(
        @PathVariable userId: Long
    ): ResponseEntity<List<UserDTO>>{
        return ResponseEntity.ok(userService.getAllFriendsById(userId))
    }
}