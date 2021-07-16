package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dao.PostDao
import ru.netology.dto.Post

class PostRepositoryInSQLite(private val dao: PostDao) : PostRepository {

    private var posts = emptyList<Post>()
    private var data = MutableLiveData(posts)

    init {
        data = MutableLiveData(dao.getAll())
    }

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        dao.likeById(id)
        val posts = data.value?.map {
            if (it.id != id) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
        posts = posts.map {
            if (it.id != id) it else it.copy(shares = it.shares + 1)
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        val posts = data.value?.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
             posts.map {
                 if (it.id != id) it else
                     it.copy(content = post.content)
             }
        }
        data.value = posts
    }

    override fun edit(post: Post) {
        data.value = posts
    }

    override fun video() {
        data.value = posts
    }

}