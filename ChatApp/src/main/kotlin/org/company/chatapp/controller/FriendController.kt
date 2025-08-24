package org.company.chatapp.controller

import org.company.chatapp.DTO.FriendsDTO
import org.company.chatapp.service.FriendService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/friends")
class FriendController(
    val friendService: FriendService
){
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
        } catch (e: ResponseStatusException){
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
        } catch (e: ResponseStatusException){
            ResponseEntity.badRequest().body(e.message)
        }

    }

    // (Tuỳ chọn) Huỷ lời mời kết bạn
    @PostMapping("/cancel/{friendshipId}")
    fun cancelFriendRequest(
        @PathVariable friendshipId: Long
    ): ResponseEntity<String> {
        friendService.cancelFriendRequest(friendshipId)
        return try {
            ResponseEntity.ok("Đã huỷ lời mời kết bạn")
        } catch (e: ResponseStatusException){
            ResponseEntity.badRequest().body(e.message)
        }
    }

    @GetMapping("/pending/{userId}")
    fun listPending(
        @PathVariable userId: Long
    ): ResponseEntity<List<FriendsDTO?>> {
        val pending = friendService.getAllPendingFriends(userId)
        return try {
            ResponseEntity.ok(pending)
        } catch (e: ResponseStatusException){
            ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/request/{userId}")
    fun listRequests(
        @PathVariable userId: Long
    ): ResponseEntity<List<FriendsDTO?>> {
        val request = friendService.getAllRequestedFriends(userId)
        return try {
            ResponseEntity.ok(request)
        } catch (e: ResponseStatusException){
            ResponseEntity.badRequest().build()
        }
    }

    @GetMapping("/getFriendByEmail/{userId}")
    fun getFriendByEmail(
        @PathVariable userId: Long,
        @RequestParam email: String
    ): ResponseEntity<FriendsDTO>{
        val friend = friendService.getFriendByEmail(userId, email)
        return if (friend != null){
            ResponseEntity.ok(friend)
        } else{
            ResponseEntity.notFound().build()
        }
    }

}