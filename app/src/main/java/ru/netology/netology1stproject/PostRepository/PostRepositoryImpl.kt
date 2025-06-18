package ru.netology.nmedia.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okio.IOException
import ru.netology.netology1stproject.api.PostsApi
import ru.netology.netology1stproject.dao.PostDao
import ru.netology.netology1stproject.dto.Post
import ru.netology.netology1stproject.entity.PostEntity
import ru.netology.netology1stproject.entity.toDto
import ru.netology.netology1stproject.entity.toEntity
import ru.netology.netology1stproject.error.ApiError
import ru.netology.netology1stproject.error.AppError
import ru.netology.netology1stproject.error.NetworkError
import ru.netology.netology1stproject.error.UnknownError

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {
    override val data = dao.getAll()
        .map(List<PostEntity>::toDto)
        .flowOn(Dispatchers.Default)

    override suspend fun getAllAsync() {
        try {
            val response = PostsApi.retrofitService.getAll()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), "Пустое тело ответа")
            // Сохранение данных в локальную БД
            dao.insert(body.map { PostEntity.fromDto(it) })
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun likeByIdAsync(id: Long) {
        val post = dao.getById(id)
        dao.likeById(id)
        try {
            // Отправка на сервер
            val response = PostsApi.retrofitService.likeById(id)
            if (!response.isSuccessful) {
                dao.insert(post)
                throw ApiError(response.code(), response.message())
            }
        } catch (e: Exception) {
            dao.insert(post)
            throw e
        }
    }

    override suspend fun unlikeByIdAsync(id: Long) {
        val post = dao.getById(id)
        dao.likeById(id)
        try {

            val response = PostsApi.retrofitService.dislikeById(id)
            if (!response.isSuccessful) {
                dao.insert(post)
                throw ApiError(response.code(), response.message())
            }
        } catch (e: Exception) {
            dao.insert(post)
            throw e
        }
    }

    override suspend fun shareByIdAsync(id: Long) {

        return try {
            val response = PostsApi.retrofitService.shareById(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            response.body() ?: throw ApiError(response.code(), "Пустое тело ответа")
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun removeByIdAsync(id: Long) {
        val post = dao.getById(id)
        dao.removeById(id)

        try {
            // Отправка на сервер
            val response = PostsApi.retrofitService.removeById(id)
            if (!response.isSuccessful) {
                dao.insert(post) // Восстановить в БД
                throw ApiError(response.code(), response.message())
            }
        } catch (e: Exception) {
            dao.insert(post) // Восстановить в БД
            throw e
        }
    }

    override suspend fun saveAsync(post: Post): Post {
        val tempId = -System.currentTimeMillis()
        val draftPost = post.copy(id = tempId, isSynced = false)
        val draftEntity = PostEntity.fromDto(draftPost)
        dao.insert(draftEntity)

        try {
            val response = PostsApi.retrofitService.save(post)
            if (!response.isSuccessful) {
                dao.removeById(tempId)
                throw ApiError(response.code(), response.message())
            }

            val syncedPost = response.body() ?: throw UnknownError()
            val syncedEntity = PostEntity.fromDto(syncedPost.copy(isSynced = true))

            dao.removeById(tempId)
            dao.insert(syncedEntity)
            return syncedPost
        } catch (e: Exception) {
            dao.removeById(tempId)
            throw NetworkError
        }
    }

    override fun getNewerCount(id: Long): Flow<Int> = flow {
        while (true) {

            delay(10_000L)
            val response = PostsApi.retrofitService.getNewer(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }

            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(body.toEntity())
            emit(body.size)
        }
    }
        .catch { e -> throw AppError.from(e) }
        .flowOn(Dispatchers.Default)

    override suspend fun markAllAsShown() {
        withContext(Dispatchers.IO) {
            dao.markAllAsShown()
        }
    }
}
