package ru.netology.dao

import androidx.lifecycle.LiveData
import ru.netology.dto.Post

interface PostDao {
    fun getAll(): List<Post>
    fun likeById(id: Long)
    fun shareById(id: Long)
    fun removeById(id: Long)

    fun save(post: Post): Post
    fun edit(post: Post)

    fun video()
}