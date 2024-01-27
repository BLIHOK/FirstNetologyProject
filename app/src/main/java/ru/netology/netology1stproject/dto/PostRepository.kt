package ru.netology.netology1stproject.dto

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface PostRepository {
    fun get(): LiveData<Post>
    fun like()
    fun share()
}

class PostRepositoryInMemoryImpl : PostRepository {
    private var post = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология!",
        published = "21 мая в 18:36",
        likeCount = 100,
        likedByMe = false,
        shareCount = 999,
        shareByMe = false
    )
    private val data = MutableLiveData(post)
    override fun get(): LiveData<Post> = data

    override fun like() {
        post = post.copy(
            likedByMe = !post.likedByMe,
            likeCount = if (post.likedByMe) post.likeCount - 1 else post.likeCount + 1
        )
        data.value = post
    }

    override fun share() {
        post = post.copy(
            shareByMe = post.shareByMe,
            shareCount = post.shareCount + 1
        )
        data.value = post
    }

}