package org.company.chatapp.repository

import org.company.chatapp.entity.UserFcmTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UserFcmTokenRepository: JpaRepository<UserFcmTokenEntity, Long> {
    @Query("SELECT * FROM user_fcm_token WHERE user_id = :userId", nativeQuery = true)
    fun findByUserId(@Param("userId") userId: Long): List<UserFcmTokenEntity>

    @Query("SELECT * FROM user_fcm_token WHERE user_id = :userId AND token = :token", nativeQuery = true)
    fun findByUserIdAndToken(@Param("userId") userId: Long, @Param("token") token: String): UserFcmTokenEntity?
}