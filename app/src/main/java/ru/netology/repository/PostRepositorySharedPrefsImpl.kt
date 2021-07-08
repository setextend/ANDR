package ru.netology.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.dto.Post


class PostRepositorySharedPrefsImpl(context: Context) : PostRepository {

    private val prefs = context.getSharedPreferences("rep", Context.MODE_PRIVATE)
    private val gson = Gson()
    private var posts = emptyList<Post>()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private val data = MutableLiveData(posts)
    private val key = "posts"

    private var nextId = 1L

    init {
        prefs.getString("posts", null)?.let {
            posts = gson.fromJson(it, type)
            data.value = posts
        }
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it
            else
                it.copy(
                    likes = it.likes + 1 * (if (it.likedByMe) -1 else 1),
                    likedByMe = !it.likedByMe
                )
        }
        data.value = posts
        sync()
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it
            else
                it.copy(
                    shares = it.shares + 1,
                    sharedByMe = true
                )
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    published = "Now",
                    likedByMe = false,
                    likes = 0,
                    shares = 0
                )
            ) + posts
        } else {
            posts = posts.map {
                if (it.id != post.id) it
                else
                    it.copy(
                        content = post.content
                    )
            }
        }
        data.value = posts
        sync()
    }

    private fun sync() {
        with(prefs.edit()) {
            putString(key, gson.toJson(posts))
            apply()
        }
    }

    override fun edit(post: Post) {
        data.value = posts
        sync()
    }

    override fun video() {
        data.value = posts
        sync()
    }

}