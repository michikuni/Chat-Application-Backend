package org.company.chatapp.controller

import org.company.chatapp.DTO.FcmTokenRequest
import org.company.chatapp.service.UserFcmTokenService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/fcm")
class UserFcmTokenController (
    private val userFcmTokenService: UserFcmTokenService
){

    @PostMapping("/save-token")
    fun saveToken(
        @RequestBody request: FcmTokenRequest
    ){
        userFcmTokenService.saveFcmToken(userId = request.userId, token = request.token)
    }

    @GetMapping("/get-token/{userId}")
    fun getToken(
        @PathVariable userId: Long,
    ): List<String>{
        return userFcmTokenService.getTokensByUserId(userId = userId)
    }
}