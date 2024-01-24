package ru.netology.netology1stproject.dto

data class Post(
    val id:Long,
    val author:String,
    val content:String,
    val published:String,
    var likeCount:Int = 0,
    var likedByMe:Boolean = false,
    var shareCount:Int = 0,
    var shareByMe:Boolean = false
)
