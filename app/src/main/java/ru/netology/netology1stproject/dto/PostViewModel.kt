package ru.netology.netology1stproject.dto

import androidx.lifecycle.ViewModel
import ru.netology.netology1stproject.repository.PostRepository
import ru.netology.netology1stproject.repository.PostRepositoryInMemoryImpl

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()

    val data = repository.getAll()
    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
}