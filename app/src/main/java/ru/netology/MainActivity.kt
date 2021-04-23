package ru.netology

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.netology.databinding.ActivityMainBinding
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likedByMe = false,
            likes = 999,
            shares = 999999
        )

        with(binding) {
            author.text = post.author
            content.text = post.content
            published.text = post.published
            likesCount.text = dealWithNumbers(post.likes)
            shareCount.text = dealWithNumbers(post.shares)
            viewsCount.text = post.views.toString()

            if (post.likedByMe) {
                likes?.setImageResource(R.drawable.ic_baseline_favorite_redone_24)
            }

            root.setOnClickListener {
                    viewsCount.text = post.views++.toString()
            }

            likes?.setOnClickListener {
                post.likedByMe = !post.likedByMe
                likes?.setImageResource(
                    if (post.likedByMe)
                        R.drawable.ic_baseline_favorite_redone_24
                    else
                        R.drawable.ic_baseline_favorite_24
                )
                if (post.likedByMe) post.likes++ else post.likes--
                likesCount.text = dealWithNumbers(post.likes)
            }

            share?.setOnClickListener {
                post.sharedByMe = true
                share?.setImageResource(
                    if (post.sharedByMe)
                        R.drawable.ic_baseline_share_redone_24
                    else
                        R.drawable.ic_baseline_share_24
                )
                if (post.sharedByMe) post.shares++
                shareCount.text = dealWithNumbers(post.shares)
            }
        }
    }



    fun dealWithNumbers(number: Long): String{
        if (number < 1000) return number.toString()
        if (number < 1000_000) return (number/1000).toDouble().toString()+"K"
        else return (number/1000_000).toDouble().toString()+"M"
    }
}