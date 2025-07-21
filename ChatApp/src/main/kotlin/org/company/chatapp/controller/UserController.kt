package org.company.chatapp.controller

import org.company.chatapp.entity.UserEntity
import org.company.chatapp.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController (
    private val userService: UserService
){
    @GetMapping("/test")
    fun test(): String = "OK"

    @GetMapping("/all_users/{userId}")
    fun allUsers(
        @PathVariable userId: Long
    ): List<UserEntity>{
        return userService.getAllFriendsById(userId)
    }
}