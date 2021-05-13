package ru.netology.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.dto.Post

class PostRepositoryInMemory : PostRepository {
    var posts = listOf(
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            likes = 999,
            shares = 999999
        ),
        Post(
            id = 2,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Курсы по веб и мобильной разработке для новичков и junior-разработчиков. Вы освоите профессию разработчика с нуля или добавите в арсенал необходимый язык программирования.",
            published = "21 июня в 10:25",
            likedByMe = false,
            likes = 998,
            shares = 1100
        ),
        Post(
            id = 3,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Курсы по веб и мобильной разработке для новичков и junior-разработчиков. Вы освоите профессию разработчика с нуля или добавите в арсенал необходимый язык программирования.",
            published = "21 июня в 10:25",
            likedByMe = true,
            likes = 1,
            shares = 1
        )
    )

    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        posts = posts.map {
            if (it.id != id) it
            else
                it.copy(
                likes = it.likes + 1 * (if (it.likedByMe) -1 else 1),
                likedByMe = !it.likedByMe )
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map {
            if (it.id != id) it
            else
                it.copy(
                    shares = it.shares + 1,
                    sharedByMe = true )
        }
        data.value = posts
    }


}