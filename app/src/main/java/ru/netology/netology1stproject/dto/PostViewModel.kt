package ru.netology.netology1stproject.dto

import androidx.lifecycle.ViewModel

//class PostViewModel(repository: PostRepository) : ViewModel() {
class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()

    val data = repository.get()
//    fun like(repository: PostRepository) = repository.like()
    fun like() = repository.like()
//    fun share(repository: PostRepository) = repository.share()
    fun share() = repository.share()
}