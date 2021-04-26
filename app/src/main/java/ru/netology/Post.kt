package ru.netology

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likes: Long = 7,
    val views: Long = 5,
    val shares: Long = 7,
    val likedByMe: Boolean = false,
    val sharedByMe: Boolean = false,
    val viewedByMe: Boolean = false
)
