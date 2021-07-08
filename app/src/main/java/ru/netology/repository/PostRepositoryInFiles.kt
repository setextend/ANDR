package ru.netology.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.dto.Post

class PostRepositoryInFiles(private val context: Context) : PostRepository {

    private val gson = Gson()
    private var posts = emptyList<Post>()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private var data = MutableLiveData(posts)
    private val FILE_NAME = "postsi.json"

    private var nextId = 1L

    init {
        val file = context.filesDir.resolve(FILE_NAME)
        if (file.exists()) {
            context.openFileInput(FILE_NAME).bufferedReader().use {
                posts = gson.fromJson(it, type)
                data.value = posts
            }
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

    fun sync() {
        context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
            it.write(gson.toJson(posts))
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