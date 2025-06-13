package ru.netology.netology1stproject.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.netology1stproject.dto.Post
import ru.netology.netology1stproject.model.FeedModel
import ru.netology.netology1stproject.model.FeedModelState
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.util.SingleLiveEvent


private val empty = Post(
    id = 0,
    content = "",
    author = "",
    authorAvatar = "",
    likedByMe = false,
    likes = 0,
    shareByMe = false,
    shareCount = 0,
    watchCount = 0,
    published = "",
    video = null,
    attachment = null,
    isSynced = false
)

class PostViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PostRepository = PostRepositoryImpl(
        AppDb.getInstance(context = application).postDao()
    )
    private val _data = MutableLiveData<FeedModel>()
//    val data: LiveData<FeedModel> = repository.data.map(::FeedModel)
//        get() = _data

    val data: LiveData<FeedModel>
        get() = _data

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(loading = true)
            repository.getAllAsync()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

    fun save() {
        edited.value?.let { post ->
            _postCreated.value = Unit
            viewModelScope.launch {
                try {
                    repository.saveAsync(post)
                    _dataState.value = FeedModelState()
                } catch (e: Exception) {
                    _dataState.value = FeedModelState(error = true)
                }
            }
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }


    fun likeById(id: Long) {
        val currentPosts = _data.value?.posts ?: emptyList()
        val postToUpdate = currentPosts.find { it.id == id } ?: return

        val updatedPost = postToUpdate.copy(
            likedByMe = !postToUpdate.likedByMe,
            likes = if (postToUpdate.likedByMe) postToUpdate.likes - 1 else postToUpdate.likes + 1
        )

        val updatedPosts = currentPosts.map { if (it.id == id) updatedPost else it }
        _data.postValue(_data.value?.copy(posts = updatedPosts)) // Обновить UI

        viewModelScope.launch {
            try {
                repository.likeByIdAsync(id)
            } catch (e: Exception) {
                // Откат изменений
                _data.postValue(_data.value?.copy(posts = currentPosts))
                _dataState.postValue(FeedModelState(error = true))
            }
        }
    }


    fun unlikeById(id: Long) {
        viewModelScope.launch {
            try {
                repository.unlikeByIdAsync(id) // Метод должен быть реализован в репозитории
                loadPosts() // Обновляем посты после лайка
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun shareById(id: Long) {
        viewModelScope.launch {
            try {
                repository.shareByIdAsync(id) // Метод должен быть реализован в репозитории
                loadPosts() // Обновляем посты после шэра
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun removeById(id: Long) {
        val oldPosts = data.value?.posts.orEmpty()
        _data.value =
            _data.value?.copy(posts = oldPosts.filter { it.id != id }) // Удаляем пост из UI
        viewModelScope.launch {
            try {
                repository.removeByIdAsync(id) // Вызываем метод репозитория
            } catch (e: Exception) {
                _data.value = _data.value?.copy(posts = oldPosts) // Восстанавливаем UI
                _dataState.value = FeedModelState(error = true) // Показ ошибки
            }
        }
    }

    fun refreshPosts() = viewModelScope.launch {
        try {
            _dataState.value = FeedModelState(refreshing = true)
            repository.getAllAsync()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
    }

    fun cancelEdit() {
        edited.value = empty
    }
}

