package org.company.chatapp.controller

import org.company.chatapp.DTO.FriendsDTO
import org.company.chatapp.DTO.UserDTO
import org.company.chatapp.service.FriendService
import org.company.chatapp.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/friends")
class FriendController(
    val friendService: FriendService
){
    @GetMapping("/test")
    fun testFriends(): String = "OK"
    @GetMapping("/test1")
    fun testAuth(): ResponseEntity<String> {
        val auth = SecurityContextHolder.getContext().authentication
        println("===> In controller, user: ${auth.name}, roles: ${auth.authorities}")
        return ResponseEntity.ok("You are authenticated")
    }

    // Gửi lời mời kết bạn
    @PostMapping("/add/{senderId}")
    fun sendFriendRequest(
        @PathVariable senderId: Long,
        @RequestBody receiverEmail: String
    ): ResponseEntity<String> {
        return try {
            println("Sender: $senderId")
            println("Sender: $receiverEmail")
            friendService.sendFriendRequest(senderId, receiverEmail)
            ResponseEntity.ok("Gửi lời mời kết bạn thành công")
        } catch (e: IllegalArgumentException){
            ResponseEntity.badRequest().body(e.message)
        }
    }

    // Chấp nhận lời mời kết bạn
    @PostMapping("/accept/{friendshipId}")
    fun acceptFriendRequest(
        @PathVariable friendshipId: Long
    ): ResponseEntity<String> {
        return try {
            friendService.acceptFriendRequest(friendshipId)
            ResponseEntity.ok("Đã chấp nhận lời mời kết bạn")
        } catch (e: IllegalArgumentException){
            ResponseEntity.badRequest().body(e.message)
        }

    }

//    // (Tuỳ chọn) Huỷ lời mời kết bạn
//    @DeleteMapping("/cancel/{friendshipId}")
//    fun cancelFriendRequest(
//        @PathVariable friendshipId: Long
//    ): ResponseEntity<String> {
//        friendService.cancelFriendRequest(friendshipId)
//        return ResponseEntity.ok("Đã huỷ lời mời kết bạn")
//    }
//
//    // (Tuỳ chọn) Lấy danh sách bạn bè của một user
//    @GetMapping("/list/{userId}")
//    fun listFriends(
//        @PathVariable userId: Long
//    ): ResponseEntity<List<UserEntity>> {
//        val friends = friendService.getAcceptedFriends(userId)
//        return ResponseEntity.ok(friends)
//    }

    @GetMapping("/pending/{userId}")
    fun listPendingRequests(
        @PathVariable userId: Long
    ): ResponseEntity<List<FriendsDTO?>> {
        val pending = friendService.getAllPendingFriends(userId)
        return ResponseEntity.ok(pending)
    }

}