package ru.netology.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.adapter.AdapterCallBack
import ru.netology.adapter.PostAdapter
import ru.netology.databinding.ActivityMainBinding
import ru.netology.dto.Post
import ru.netology.viewmodel.PostViewModel



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val adapter = PostAdapter(object : AdapterCallBack {
            override fun shared(post : Post) {
                viewModel.shareById(post.id)
            }

            override fun liked(post: Post) {
                viewModel.likeById(post.id)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(this){ posts ->
            adapter.submitList(posts)
        }

    }
}