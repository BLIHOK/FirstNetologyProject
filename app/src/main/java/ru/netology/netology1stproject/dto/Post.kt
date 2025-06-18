package ru.netology.netology1stproject.dto

import ru.netology.netology1stproject.enumiration.AttachmentType

data class Post(
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likes: Int,
    val likedByMe: Boolean,
    val shareCount: Int,
    val shareByMe: Boolean,
    val watchCount: Int,
    val video: String?,
    var attachment: Attachment?,
    val isSynced: Boolean,
    val isNew: Boolean,
)

data class Attachment(
    val url: String,
    val description: String?,
    val type: AttachmentType,
)

data class Comment(
    val id: Long,
    val postId: Long,
    val authorId: Long,
    val content: String,
    val published: Long,
    val likedByMe: Boolean,
    val likes: Int = 0,
)

data class Author(
    val id: Long,
    val name: String,
    val avatar: String,
)