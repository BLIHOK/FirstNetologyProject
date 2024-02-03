package ru.netology.netology1stproject.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.netology1stproject.dto.Post

interface PostRepository {
    fun getAll(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
}

private var nextId = 0L

class PostRepositoryInMemoryImpl : PostRepository {

    private var posts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это дополнительный пост для массива постов",
            published = "21 мая в 18:36",
            likeCount = 100,
            likedByMe = false,
            shareCount = 2,
            shareByMe = false,
            watchCount = 399
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это дополнительный пост для массива постов",
            published = "21 мая в 18:36",
            likeCount = 999,
            likedByMe = false,
            shareCount = 10000,
            shareByMe = false,
            watchCount = 550
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это дополнительный пост для массива постов",
            published = "21 мая в 18:36",
            likeCount = 0,
            likedByMe = false,
            shareCount = 0,
            shareByMe = false,
            watchCount = 3999
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это дополнительный пост для массива постов",
            published = "21 мая в 18:36",
            likeCount = 2,
            likedByMe = false,
            shareCount = 2,
            shareByMe = false,
            watchCount = 173
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это дополнительный пост для массива постов",
            published = "21 мая в 18:36",
            likeCount = 9999999,
            likedByMe = false,
            shareCount = 7,
            shareByMe = false,
            watchCount = 178359
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология!",
            published = "21 мая в 18:36",
            likeCount = 100,
            likedByMe = false,
            shareCount = 999,
            shareByMe = false,
            watchCount = 55547
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Это дополнительный пост для массива постов",
            published = "21 мая в 18:36",
            likeCount = 343,
            likedByMe = false,
            shareCount = 30,
            shareByMe = false,
            watchCount = 101017
        ),
    ).reversed()

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likeCount = if (it.likedByMe) it.likeCount - 1 else it.likeCount + 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                shareByMe = !it.shareByMe,
                shareCount = it.shareCount + 1
            )
        }
        data.value = posts
    }

    override fun save(post: Post) {
        posts = if( post.id == 0L) {
            listOf(post.copy(id = nextId++, author = "Me", published = "now")) + posts
        } else{
            posts.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }
}