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
        return when {
            number < 1000 -> number.toString()
            number < 1000_000 -> roundIfNeeded((number/1000).toDouble())+"K"
            else -> roundIfNeeded((number/1000_000).toDouble())+"M"
        }
    }

    fun roundIfNeeded(number: Double): String{
        val numberButThree = number.toString().take(3)
        return when {
            numberButThree.endsWith(".") -> numberButThree.take(2)
            numberButThree.endsWith(".0") -> numberButThree.take(1)
            else -> number.toString()
        }
    }
}