package ru.netology.nmedia.repository


import androidx.lifecycle.map
import ru.netology.netology1stproject.dao.PostDao
import ru.netology.netology1stproject.dto.Post
import ru.netology.netology1stproject.entity.PostEntity


class PostRepositoryImpl(
    private val dao: PostDao,
) : PostRepository {
    override fun getAll() = dao.getAll().map { list ->
        list.map {
            it.toDto()
        }
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        TODO("Not yet implemented")
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }
}