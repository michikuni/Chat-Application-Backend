package org.company.chatapp.service

import com.google.firebase.messaging.BatchResponse
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.MulticastMessage
import com.google.firebase.messaging.Notification
import com.google.firebase.messaging.SendResponse
import org.springframework.stereotype.Service

@Service
class NotificationService (
    private val userFcmTokenService: UserFcmTokenService,
    private val userService: UserService
){
    fun sendMessageNotification(userId: Long, messages: String, friendId: Long){
        val tokens = userFcmTokenService.getTokensByUserId(userId)
        val userName = userService.getUserById(friendId)?.name
        val message =MulticastMessage.builder()
            .addAllTokens(tokens)
            .putData("name", userName)
            .putData("userId", userId.toString())
            .putData("message", messages)
            .putData("time", System.currentTimeMillis().toString())
            .build()
        val response: BatchResponse = FirebaseMessaging.getInstance().sendEachForMulticast(message)
        if (response.failureCount > 0){
            val responses: List<SendResponse> = response.responses
            val failedTokens: MutableList<String> = mutableListOf()
            for (rp in responses){
                if (!rp.isSuccessful){
                    failedTokens.add(rp.exception.toString())
                    println("NotiService: ${rp.exception}")
                }
            }
        }
    }
}
//        tokens.forEach { token ->
//            val message = Message.builder()
//                .setNotification(
//                    Notification.builder()
//                        .setTitle(userName)
//                        .build()
//                )
//                .setToken(token)
//                .putData("id", userId.toString())
//                .putData("message", messages)
//                .putData("time", System.currentTimeMillis().toString())
//                .build()
//            FirebaseMessaging.getInstance().send(message)
//        }
