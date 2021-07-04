package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post

class PostRepositoryInMemory : PostRepository {
    private var nextId = 1L
    private var posts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            likes = 999,
            shares = 999999,
            attVideo = "https://youtu.be/EoypNFa53Ns"
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Курсы по веб и мобильной разработке для новичков и junior-разработчиков. Вы освоите профессию разработчика с нуля или добавите в арсенал необходимый язык программирования.",
            published = "21 июня в 10:25",
            likedByMe = false,
            likes = 998,
            shares = 1100,
            attVideo = ""
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Курсы по веб и мобильной разработке для новичков и junior-разработчиков. Вы освоите профессию разработчика с нуля или добавите в арсенал необходимый язык программирования.",
            published = "21 июня в 10:25",
            likedByMe = true,
            likes = 1,
            shares = 1,
            attVideo = ""
        )
    ).reversed()

    private val data = MutableLiveData(posts)

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
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
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
    }

    override fun edit(post: Post) {
        data.value = posts
    }

    override fun video() {
        data.value = posts
    }
}