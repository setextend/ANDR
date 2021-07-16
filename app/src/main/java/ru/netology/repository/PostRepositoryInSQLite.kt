package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.netology.dao.PostDao
import ru.netology.dto.Post
import ru.netology.entity.PostEntity

class PostRepositoryInSQLite(private val dao: PostDao) : PostRepository {

    override fun getAll(): LiveData<List<Post>> = Transformations.map(dao.getAll()) { list ->
        list.map {
            Post(
                it.id,
                it.author,
                it.published,
                it.content,
                it.likes,
                it.views,
                it.shares,
                it.likedByMe,
                it.sharedByMe,
                it.viewedByMe,
                it.attVideo
            )
        }
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun edit(post: Post) {
    }

    override fun video() {
    }

}