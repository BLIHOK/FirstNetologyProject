package ru.netology.netology1stproject.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.netology1stproject.dto.Post
import ru.netology.netology1stproject.model.FeedModel
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent


private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    likes = 0,
    shareByMe = false,
    shareCount = 0,
    watchCount = 0,
    published = "",
    video = null
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        // Начинаем загрузку
        _data.postValue(FeedModel(loading = true))
        repository.getAllAsync(object : PostRepository.GetAllCallback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun changeContentAndSave(content: String) {
        edited.value?.let { post ->
            val newContent = content.trim()
            if (newContent == post.content) {
                _postCreated.postValue(Unit)
                edited.postValue(empty)
                return
            }

            val newPost = post.copy(content = newContent)
            repository.saveAsync(newPost, object : PostRepository.GetAllCallback<Unit> {
                override fun onSuccess(post: Unit) {
                    _postCreated.postValue(Unit)
                    edited.postValue(empty)
                    loadPosts() // Обновляем список после сохранения
                }

                override fun onError(e: Exception) {
                    _data.postValue(_data.value?.copy(error = true))
                }
            })
        }
    }


    fun edit(post: Post) {
        edited.value = post
    }

    fun likeById(id: Long) {
        repository.likeByIdAsync(id, object : PostRepository.GetAllCallback<Unit> {
            override fun onSuccess(post: Unit) {
                loadPosts() // Обновляем список после успеха
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true)) // Показываем ошибку
            }
        })
    }

    fun unlikeById(id: Long) {
        repository.unlikeByIdAsync(id, object : PostRepository.GetAllCallback<Unit> {
            override fun onSuccess(post: Unit) {
                loadPosts()
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun shareById(id: Long) {
        repository.shareByIdAsync(id, object : PostRepository.GetAllCallback<Unit> {
            override fun onSuccess(post: Unit) {
                loadPosts()
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun removeById(id: Long) {
        val oldPosts = _data.value?.posts.orEmpty()
        _data.postValue(
            _data.value?.copy(
                posts = oldPosts.filter { it.id != id }
            )
        )

        repository.removeByIdAsync(id, object : PostRepository.GetAllCallback<Unit> {
            override fun onSuccess(post: Unit) {
            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = oldPosts))
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun cancelEdit() {
        edited.value = empty
    }
}

