package org.company.chatapp.repository

import org.company.chatapp.DTO.FriendshipStatus
import org.company.chatapp.entity.FriendsEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface FriendshipRepository : JpaRepository<FriendsEntity, Long> {
    fun findByUserIdAndFriendId(user: Long, friend: Long): FriendsEntity?
    @Query("""
    SELECT f FROM FriendsEntity f
    WHERE (f.user.id = :userId AND f.friend.id = :friendId)
       OR (f.user.id = :friendId AND f.friend.id = :userId)
""")
    fun findBetweenUsers(userId: Long, friendId: Long): FriendsEntity?


    fun findAllByUserIdAndStatus(userId: Long, status: FriendshipStatus): List<FriendsEntity>

    fun findAllByFriendIdAndStatus(friendId: Long, status: FriendshipStatus): List<FriendsEntity>

}