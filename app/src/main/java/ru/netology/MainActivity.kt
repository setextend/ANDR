package ru.netology

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.databinding.ActivityMainBinding
import ru.netology.viewmodel.PostViewModel
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        viewModel.data.observe(this) { post ->

            with(binding) {
                author.text = post.author
                content.text = post.content
                published.text = post.published

                likes.setImageResource(
                    if (post.likedByMe)
                        R.drawable.ic_baseline_favorite_redone_24
                    else
                        R.drawable.ic_baseline_favorite_24
                )

                likesCount.text = dealWithNumbers(post.likes)

                share.setImageResource(
                    if (post.sharedByMe)
                        R.drawable.ic_baseline_share_redone_24
                    else
                        R.drawable.ic_baseline_share_24
                )

                shareCount.text = dealWithNumbers(post.shares)
                viewsCount.text = dealWithNumbers(post.views)
            }
        }
        with(binding) {
            likes.setOnClickListener {
                viewModel.like()
            }
            share.setOnClickListener {
                viewModel.share()
            }
        }
    }


    fun dealWithNumbers(number: Long): String {
        return when {
            number < 1000 -> number.toString()
            number < 1000_000 -> roundIfNeeded((number / 1000).toDouble()) + "K"
            else -> roundIfNeeded((number / 1000_000).toDouble()) + "M"
        }
    }

    fun roundIfNeeded(number: Double): String {
        val numberButThree = number.toString().take(3)
        return when {
            numberButThree.endsWith(".") -> numberButThree.take(2)
            numberButThree.endsWith(".0") -> numberButThree.take(1)
            else -> number.toString()
        }
    }
}