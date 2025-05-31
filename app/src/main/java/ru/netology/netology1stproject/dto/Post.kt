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
)

data class Attachment(
    val url: String,
    val description: String?,
    val type: AttachmentType,
)

