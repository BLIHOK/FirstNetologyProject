package ru.netology.netology1stproject.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.netology1stproject.dto.Attachment
import ru.netology.netology1stproject.dto.Post
import ru.netology.netology1stproject.enumiration.AttachmentType


@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likeCount: Int = 0,
    val likedByMe: Boolean,
    val shareCount: Int,
    val shareByMe: Boolean,
    val watchCount: Int,
    val video: String?,
    @Embedded
    var attachment: AttachmentEmbeddable?,
    val isSynced: Boolean,
    val isNew: Boolean,
) {
    fun toDto() = Post(
        id,
        author,
        authorAvatar,
        content,
        published,
        likeCount,
        likedByMe,
        shareCount,
        shareByMe,
        watchCount,
        video,
        attachment?.toDto(),
        isSynced,
        isNew,
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.author,
                dto.authorAvatar,
                dto.content,
                dto.published,
                dto.likes,
                dto.likedByMe,
                dto.shareCount,
                dto.shareByMe,
                dto.watchCount,
                dto.video,
                AttachmentEmbeddable.fromDto(dto.attachment),
                dto.isSynced,
                dto.isNew,
            )

    }
}


data class AttachmentEmbeddable(

    var url: String,
    var description: String?,
    var type: AttachmentType,
) {
    fun toDto() = Attachment(url, description, type)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.description, it.type)
        }
    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)