package ru.netology.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import ru.netology.R
import ru.netology.adapter.AdapterCallBack
import ru.netology.adapter.PostAdapter
import ru.netology.databinding.ActivityMainBinding
import ru.netology.dto.Post
import ru.netology.utils.AndroidUtils
import ru.netology.viewmodel.PostViewModel


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val editPostLauncher = registerForActivityResult(EditPostResultContract()) { result ->
            result?: return@registerForActivityResult
            viewModel.changeContent(result)
            viewModel.save()
        }

        val adapter = PostAdapter(object : AdapterCallBack {
            override fun shared(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }
                viewModel.shareById(post.id)
                val shareIntent = Intent.createChooser(intent, post.author)
                startActivity(shareIntent)
            }

            override fun liked(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun deleted(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun edited(post: Post) {
                viewModel.edit(post)
                editPostLauncher.launch(post.content)
            }
        })

        binding.list.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
            binding.list.scrollToPosition(0)
        }


        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContent(result)
            viewModel.save()
        }

        binding.fab.setOnClickListener {
            newPostLauncher.launch()
        }

    }
}