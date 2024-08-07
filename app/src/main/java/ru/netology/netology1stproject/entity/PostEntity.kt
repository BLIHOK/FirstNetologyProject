package ru.netology.netology1stproject.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.netology1stproject.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likeCount: Int = 0,
    val likedByMe: Boolean,
    val shareCount: Int,
    val shareByMe: Boolean,
    val watchCount: Int,
    val video: String?,
) {
    fun toDto() = Post(id, author, content, published, likeCount, likedByMe, shareCount, shareByMe, watchCount, video)

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.author, dto.content, dto.published, dto.likeCount, dto.likedByMe, dto.shareCount, dto.shareByMe, dto.watchCount, dto.video)

    }
}