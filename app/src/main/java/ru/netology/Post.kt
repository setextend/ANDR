package ru.netology

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    var likedByMe: Boolean = false,
    var likes: Long = 7,
    var views: Long = 5,
    var shares: Long = 7,
    var sharedByMe: Boolean = false

)
