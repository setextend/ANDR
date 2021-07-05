package ru.netology.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.dto.Post
import ru.netology.repository.PostRepository
import ru.netology.repository.PostRepositoryInMemory
import ru.netology.repository.PostRepositorySharedPrefsImpl

private val emptyPost = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likes = 0,
    views = 0,
    shares = 0,
    likedByMe = false,
    sharedByMe = false,
    viewedByMe = false,
    attVideo = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository = PostRepositorySharedPrefsImpl(application)
    val data = repository.getAll()
    val edited = MutableLiveData(emptyPost)

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)

    fun removeById(id: Long) = repository.removeById(id)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = emptyPost
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content != text) {
                edited.value = edited.value?.copy(content = text)
            }
        }
    }
}