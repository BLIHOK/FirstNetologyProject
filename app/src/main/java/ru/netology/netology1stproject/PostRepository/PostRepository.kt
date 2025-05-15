package ru.netology.nmedia.repository

import ru.netology.netology1stproject.dto.Post

interface PostRepository {
//    fun getAll(): List<Post>
//    fun likeById(id: Long)
//    fun unlikeById(id: Long)
//    fun shareById(id: Long)
//    fun save(post: Post)
//    fun removeById(id: Long)

    fun getAllAsync(callback: GetAllCallback)
    fun likeByIdAsync(id: Long, callback: SingleOperationCallback)
    fun unlikeByIdAsync(id: Long, callback: SingleOperationCallback)
    fun shareByIdAsync(id: Long, callback: SingleOperationCallback)
    fun saveAsync(post: Post, callback: SingleOperationCallback)
    fun removeByIdAsync(id: Long, callback: SingleOperationCallback)

    interface GetAllCallback {
        fun onSuccess(posts: List<Post>)
        fun onError(e: Exception)
    }

    interface SingleOperationCallback {
        fun onSuccess()
        fun onError(e: Exception)
    }
}