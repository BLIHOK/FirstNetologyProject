package ru.netology.netology1stproject.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.netology1stproject.dto.Post

//interface PostRepository {
//    fun getAll(): LiveData<List<Post>>
//    fun likeById(id: Long)
//    fun shareById(id: Long)
//    fun removeById(id: Long)
//    fun save(post: Post)
//}



class PostRepositorySharedPreference (context: Context) : PostRepository {
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val key = "posts"
    private var nextId = 0L
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)


    init {
        prefs.getString(key, null)?.let {
            posts = gson.fromJson(it, typeToken)
            nextId = posts.maxOfOrNull { it.id }?.inc() ?: 1
            data.value = posts
        }
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likeCount = if (it.likedByMe) it.likeCount - 1 else it.likeCount + 1
            )
        }
        data.value = posts
        sync()
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it else it.copy(
                shareByMe = !it.shareByMe,
                shareCount = it.shareCount + 1
            )
        }
        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(post.copy(id = nextId++, author = "Me", published = "now")) + posts
        } else {
            posts.map {
                if (it.id != post.id) it else it.copy(content = post.content)
            }
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    private fun sync () {
        prefs.edit().apply{
            putString(key, gson.toJson(posts))
            apply()
        }
    }

}