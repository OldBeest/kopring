package com.example.kopring.board.service

import com.example.kopring.board.dto.PostDto
import com.example.kopring.board.dto.ReplyCountDto
import com.example.kopring.board.dto.ReplyDto
import com.example.kopring.board.entity.NoticeEntities
import com.example.kopring.board.repository.NoticeRepository
import com.example.kopring.board.repository.PostRepository
import com.example.kopring.board.entity.PostEntities
import com.example.kopring.board.entity.ThumbsUpEntities
import com.example.kopring.board.repository.ReplyRepository
import com.example.kopring.board.repository.ThumbsUpRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class PostService(
    private val postRepository: PostRepository,
    private val noticeRepository: NoticeRepository,
    private val replyRepository: ReplyRepository,
    private val thumbsUpRepository: ThumbsUpRepository
) {
    // 공지사항 부분
    fun getNoticeList(): List<NoticeEntities> = noticeRepository.getNoticeList()

    fun getList(): List<PostEntities> = postRepository.findAllByOrderByPostNoDescPostGroupDescPostStepAsc()

    fun getCategoryList(category: String, value: String): List<PostEntities> = postRepository.findAllByPostTitleContains(category, value)

    fun countRepliesByPostNo(): List<ReplyCountDto>{
        var replyCountList: List<Any> = replyRepository.countRepliesByPostNo()
        var replyCountResult: List<ReplyCountDto> = replyCountList.map{ new ->
            val (postNo, count) = new as Array<*>
            ReplyCountDto(postNo as Int, count as Long)}
        return replyCountResult
    }

    fun getPost(postNo: Int?): PostDto?{
        return postRepository.findByPostNo(postNo)?.let { PostDto.fromEntity(it) }
    }

    fun createPost(postDto: PostDto): Unit{
        postRepository.save(postDto.toEntity(postDto))
    }

    fun updatePost(postDto: PostDto): Unit{
        postRepository.save(postDto.toEntity(postDto))
    }

    @Transactional
    fun deletePost(postNo: Int): Unit{
        postRepository.deleteByPostNo(postNo)
    }

    fun getReplyList(postNo: Int): List<ReplyDto>?{
        return replyRepository.findAllByPostNoOrderByReplyOrderDesc(postNo)?.map{ReplyDto.fromEntity(it)}
    }

    fun createReply(replyDto: ReplyDto): Unit{
        replyRepository.save(replyDto.toEntity(replyDto))
    }

    fun updateReply(replyDto: ReplyDto): Unit{
        replyRepository.save(replyDto.toEntity(replyDto))
    }
    @Transactional
    fun deleteReply(replyId: Int): Unit{
        replyRepository.deleteByReplyId(replyId)
    }

    fun getReplyMaxId(): Int?{
        return replyRepository.findMaxReplyId()
    }

    fun checkThumbsUp(replyId: Int, userId: String): ThumbsUpEntities?{
        val thumbsUpEntities = ThumbsUpEntities()
        thumbsUpEntities.replyId = replyId
        thumbsUpEntities.userId = userId
        return thumbsUpRepository.findByReplyIdAndUserId(replyId, userId)
    }

    fun thumbsUpReply(replyId: Int, userId: String): Unit{
        val thumbsUpEntities = ThumbsUpEntities()
        thumbsUpEntities.replyId = replyId
        thumbsUpEntities.userId = userId
        return thumbsUpRepository.save(thumbsUpEntities)
    }



}