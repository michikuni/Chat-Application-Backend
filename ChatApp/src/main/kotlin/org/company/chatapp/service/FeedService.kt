package org.company.chatapp.service

import org.company.chatapp.DTO.FeedDTO
import org.company.chatapp.entity.FeedEntity
import org.company.chatapp.entity.UserEntity
import org.company.chatapp.repository.FeedRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class FeedService (
    private val feedRepository: FeedRepository,
    private val customMapper: CustomMapper
){
    fun createFeed(feed: FeedEntity): FeedEntity {
        if (feed.content.isNullOrBlank() && feed.mediaFile.isNullOrBlank()) {
            throw IllegalArgumentException("Feed must have either content or media file.")
        }
        return feedRepository.save(feed)
    }

    fun getAllFeedByUserId(userId: Long): List<FeedDTO>{
        val feeds = feedRepository.findAllByPosterId(userId)
        return feeds.map { customMapper.feedDTO(it) }
    }
}