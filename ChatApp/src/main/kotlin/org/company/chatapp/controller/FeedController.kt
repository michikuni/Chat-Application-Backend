package org.company.chatapp.controller

import org.company.chatapp.DTO.FeedDTO
import org.company.chatapp.entity.FeedEntity
import org.company.chatapp.service.FeedService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Instant

@RestController
@RequestMapping("/api/feed/")
class FeedController (
    val feedService: FeedService
){
    @PostMapping("/post_newsfeed/{userId}", consumes = ["multipart/form-data"])
    fun postNewsFeed(
        @PathVariable userId: Long,
        @RequestParam("content", required = false) content: String?,
        @RequestParam("mediaFile", required = false) mediaFile: MultipartFile?
    ): ResponseEntity<Map<String, String>> {
        return try {
            if (content.isNullOrBlank() && (mediaFile == null || mediaFile.isEmpty)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(mapOf("error" to "Feed must have either content or media file."))
            }

            // Tạo thư mục lưu file
            val uploadDir = Paths.get(System.getProperty("user.dir"), "feed_media", userId.toString()).toFile()
            if (!uploadDir.exists()) uploadDir.mkdirs()

            var savedFilename: String? = null

            // Nếu có file upload
            if (mediaFile != null && !mediaFile.isEmpty) {
                val originalFilename = mediaFile.originalFilename
                val extension = originalFilename?.substringAfterLast('.', "")
                val filename = "${userId}_${System.currentTimeMillis()}${if (!extension.isNullOrEmpty()) ".$extension" else ""}"

                val filePath = File(uploadDir, filename)
                mediaFile.transferTo(filePath)
                savedFilename = filename
            }

            // Tạo feed entity
            val feed = FeedEntity(
                posterId = userId,
                content = content,
                mediaFile = savedFilename,
                createdAt = Instant.now()
            )

            feedService.createFeed(feed)

            // ✅ Trả về phản hồi thành công
            ResponseEntity.ok(mapOf("message" to "Feed created successfully."))
        } catch (ex: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to (ex.message ?: "Unexpected error occurred.")))
        }
    }


    @GetMapping("/getMediaFile/{fileName}")
    fun getMediaFile(
        @PathVariable fileName: String
    ): ResponseEntity<ByteArray> {
        return try {
            val parts = fileName.split("_")
            val userId = parts[0]
            val imageFile = Paths.get(System.getProperty("user.dir"), "feed_media/$userId", fileName).toFile()

            if (!imageFile.exists()){
                return ResponseEntity.notFound().build()
            }

            val bytes = imageFile.readBytes()
            val contentType = Files.probeContentType(imageFile.toPath()) ?: "application/octet-stream"

            ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(bytes)
        } catch (e: Exception){
            ResponseEntity.status(500).build()
        }
    }

    @GetMapping("/getAllFeed/{userId}")
    fun getAllFeed(
        @PathVariable userId: Long,
    ): ResponseEntity<List<FeedDTO>> {
        return ResponseEntity.ok(feedService.getAllFeedByUserId(userId))
    }
}