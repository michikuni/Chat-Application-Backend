package org.company.chatapp.service

import org.company.chatapp.entity.UserFcmTokenEntity
import org.company.chatapp.repository.UserFcmTokenRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class UserFcmTokenService (
    private val userFcmTokenRepository: UserFcmTokenRepository
){
    fun saveFcmToken(userId: Long, token: String){
        val exist = userFcmTokenRepository.findByUserIdAndToken(userId, token)
        if (exist==null){
            userFcmTokenRepository.save(UserFcmTokenEntity(userId = userId, token = token, createdAt = Instant.now()))
        }
    }

    fun getTokensByUserId(userId: Long): List<String>{
        return userFcmTokenRepository.findByUserId(userId).map { it.token }
    }

    fun deleteToken(token: String){
        userFcmTokenRepository.findAll().firstOrNull{ it.token == token }?.let { userFcmTokenRepository.delete(it) }
    }
}