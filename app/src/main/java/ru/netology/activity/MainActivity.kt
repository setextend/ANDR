package ru.netology.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
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

        val adapter = PostAdapter(object : AdapterCallBack {
            override fun shared(post: Post) {
                viewModel.shareById(post.id)
            }

            override fun liked(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun deleted(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun edited(post: Post) {
                viewModel.edit(post)
            }
        })

        binding.list.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
            binding.list.scrollToPosition(0)
        }

        viewModel.edited.observe(this) {
            if (it.id != 0L) {
                with(binding.edtContent) {
                    requestFocus()
                    setText(it.content)
                }
            }
        }

        binding.btnSave.setOnClickListener {
            with(binding.edtContent) {
                if (text.isNullOrBlank()) {
                    Toast.makeText(
                        this@MainActivity,
                        R.string.error_empty_content,
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                viewModel.changeContent(text.toString())
                viewModel.save()

                setText("")
                clearFocus()
                AndroidUtils.hideKeyboard(it)
            }
        }

        binding.edtContent.doAfterTextChanged {
            if(viewModel.edited.value?.content != it.toString()) {
                binding.group.visibility = View.VISIBLE
            } else {
                binding.group.visibility  = View.INVISIBLE
            }
            with(binding.txtAuthor){
                setText(viewModel.edited.value?.author)
            }
        }

        binding.btnCancel.setOnClickListener {
            with(binding.edtContent) {
                setText(viewModel.edited.value?.content)
            }
        }
    }
}