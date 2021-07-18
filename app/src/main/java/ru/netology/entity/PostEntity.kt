package ru.netology.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.netology.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Long = 0,
    val views: Long = 0,
    val shares: Long = 0,
    val likedByMe: Boolean = false,
    val sharedByMe: Boolean = false,
    val viewedByMe: Boolean = false,
    val attVideo: String
) {
    fun toDto() = Post(
        id,
        author,
        published,
        content,
        likes,
        views,
        shares,
        likedByMe,
        sharedByMe,
        viewedByMe,
        attVideo
    )

    companion object {
        fun fromDto(dto:Post) =
            PostEntity(
                dto.id,
                dto.author,
                dto.published,
                dto.content,
                dto.likes,
                dto.views,
                dto.shares,
                dto.likedByMe,
                dto.sharedByMe,
                dto.viewedByMe,
                dto.attVideo
            )
    }
}