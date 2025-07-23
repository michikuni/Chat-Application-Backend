package org.company.chatapp.repository

import org.company.chatapp.entity.FriendsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface FriendshipRepository : JpaRepository<FriendsEntity, Long> {

    //Kiểm tra quan hệ bạn bè đã tồn tại chưa
    @Query("""
    SELECT f FROM FriendsEntity f
    WHERE (f.user.id = :userId AND f.friend.id = :friendId)
       OR (f.user.id = :friendId AND f.friend.id = :userId)
""")
    fun findBetweenUsers(userId: Long, friendId: Long): FriendsEntity?

    //Lấy danh sách bạn bè
    @Query(value = "SELECT id FROM friends WHERE (user_id = :userId OR friend_id = :userId) AND status = 'ACCEPTED'", nativeQuery = true)
    fun findAllFriendshipIdByUserId(@Param("userId") userId: Long): List<Long>?
    @Query(value = "SELECT * FROM friends WHERE id = :id", nativeQuery = true)
    fun findFriendshipById(@Param("id") id: Long): FriendsEntity?

    //Lấy danh sách yêu cầu kết bạn
    @Query(value = "SELECT user_id FROM friends WHERE friend_id = :userId AND status = 'PENDING'", nativeQuery = true)
    fun findPendingFriendIdByUserId(@Param("userId") userId: Long): List<Long>?
    @Query(value = "SELECT friend_id FROM friends WHERE user_id = :userId AND status = 'PENDING'", nativeQuery = true)
    fun findRequestedFriendIdByUserId(@Param("userId") userId: Long): List<Long>?

    @Query(value = "SELECT friend_id FROM friends WHERE user_id = :userId AND status = 'DECLINED'", nativeQuery = true)
    fun findDeclinedFriendIdByUserId(@Param("userId") userId: Long): List<Long>?

    @Query(value = "SELECT friend_id FROM friends WHERE user_id = :userId AND status = 'BLOCKED'", nativeQuery = true)
    fun findBlockedFriendIdByUserId(@Param("userId") userId: Long): List<Long>?
}