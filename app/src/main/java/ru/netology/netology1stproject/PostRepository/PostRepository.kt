package ru.netology.nmedia.repository

import ru.netology.netology1stproject.dto.Post

interface PostRepository {
//    fun getAll(): List<Post>
//    fun likeById(id: Long)
//    fun unlikeById(id: Long)
//    fun shareById(id: Long)
//    fun save(post: Post)
//    fun removeById(id: Long)

    fun getAllAsync(callback: GetAllCallback<List<Post>>)
    fun likeByIdAsync(id: Long, callback: GetAllCallback<Unit>)
    fun unlikeByIdAsync(id: Long, callback: GetAllCallback<Unit>)
    fun shareByIdAsync(id: Long, callback: GetAllCallback<Unit>)
    fun saveAsync(post: Post, callback: GetAllCallback<Unit>)
    fun removeByIdAsync(id: Long, callback: GetAllCallback<Unit>)

    interface GetAllCallback<T> {
        fun onSuccess(posts: T)
        fun onError(e: Exception)
    }

//    interface SingleOperationCallback {
//        fun onSuccess()
//        fun onError(e: Exception)
//    }
}