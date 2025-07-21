package org.company.chatapp.repository
import org.company.chatapp.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findByUsername(username: String): UserEntity?
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}