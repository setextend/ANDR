package ru.netology.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.R
import ru.netology.databinding.CardPostBinding
import ru.netology.dto.Post


interface AdapterCallBack {
    fun liked(post: Post) {}
    fun shared(post: Post) {}
    fun deleted(post: Post) {}
    fun edited(post: Post) {}
    fun video(post: Post) {}
}

class PostAdapter(private val listener: AdapterCallBack) :
    ListAdapter<Post, PostViewHolder>(PostDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

}

class PostViewHolder(private val binding: CardPostBinding, private val listener: AdapterCallBack) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            content.text = post.content
            published.text = post.published

            if (!post.attVideo.isEmpty())
                videoView.visibility = View.VISIBLE
            else videoView.visibility = View.GONE
            videoView.text = post.attVideo


            likes.isChecked = post.likedByMe

            likes.text = dealWithNumbers(post.likes)

            likes.setOnClickListener {
                listener.liked(post)
            }

            share.isChecked = post.sharedByMe

            share.setOnClickListener {
                listener.shared(post)
            }

            share.text = dealWithNumbers(post.shares)
            viewsCount.text = dealWithNumbers(post.views)

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.option_post)
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.menu_edit -> {
                                listener.edited(post)
                                true
                            }
                            R.id.menu_remove -> {
                                listener.deleted(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            videoView.setOnClickListener {
                listener.video(post)
            }

        }
    }

    private fun dealWithNumbers(number: Long): String {
        return when {
            number < 1000 -> number.toString()
            number < 1000_000 -> roundIfNeeded((number / 1000).toDouble()) + "K"
            else -> roundIfNeeded((number / 1000_000).toDouble()) + "M"
        }
    }

    private fun roundIfNeeded(number: Double): String {
        val numberButThree = number.toString().take(3)
        return when {
            numberButThree.endsWith(".") -> numberButThree.take(2)
            numberButThree.endsWith(".0") -> numberButThree.take(1)
            else -> number.toString()
        }
    }

}

class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}