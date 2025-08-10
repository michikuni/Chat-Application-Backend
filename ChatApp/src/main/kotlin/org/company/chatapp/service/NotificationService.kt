package org.company.chatapp.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class NotificationService (
    private val userFcmTokenService: UserFcmTokenService,
    private val userService: UserService
){
    fun sendMessageNotification(userId: Long, messages: String){
        val tokens = userFcmTokenService.getTokensByUserId(userId)
        val userName = userService.getUserById(userId)?.name
        tokens.forEach { token ->
            val message = Message.builder()
                .setNotification(
                    Notification.builder()
                        .setTitle(userName)
                        .build()
                )
                .setToken(token)
                .putData("id", userId.toString())
                .putData("message", messages)
                .putData("time", System.currentTimeMillis().toString())
                .build()
            FirebaseMessaging.getInstance().send(message)
        }
    }
}