package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import ru.netology.netology1stproject.dto.Post

interface PostRepository {
    val data: LiveData<List<Post>>

    suspend fun getAllAsync()
    suspend fun likeByIdAsync(id: Long)
    suspend fun unlikeByIdAsync(id: Long)
    suspend fun shareByIdAsync(id: Long)
    suspend fun saveAsync(post: Post): Post
    suspend fun removeByIdAsync(id: Long)

}