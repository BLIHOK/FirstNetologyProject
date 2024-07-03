package ru.netology.netology1stproject.dto

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likeCount: Int,
    val likedByMe: Boolean,
    val shareCount: Int,
    val shareByMe: Boolean,
    val watchCount: Int,
    val video: String?,
)
