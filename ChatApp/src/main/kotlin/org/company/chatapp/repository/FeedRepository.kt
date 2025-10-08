package org.company.chatapp.repository

import org.company.chatapp.entity.FeedCommentEntity
import org.company.chatapp.entity.FeedEntity
import org.company.chatapp.entity.FeedReactionEntity
import org.company.chatapp.entity.FeedShareEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface FeedRepository : JpaRepository<FeedEntity, Long>{
    @Query("SELECT * FROM feeds WHERE poster_id = :userId", nativeQuery = true)
    fun findAllByPosterId(@Param("userId") userId: Long): List<FeedEntity>
}
interface FeedReactionRepository : JpaRepository<FeedReactionEntity, Long>
interface FeedCommentRepository : JpaRepository<FeedCommentEntity, Long>
interface FeedShareRepository : JpaRepository<FeedShareEntity, Long>
