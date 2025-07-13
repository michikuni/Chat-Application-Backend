package org.company.chatapp.repository
import org.company.chatapp.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByAccount(username: String): UserEntity?
    fun existsByAccount(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}