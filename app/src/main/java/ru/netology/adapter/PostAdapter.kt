package ru.netology.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.netology.R
import ru.netology.databinding.CardPostBinding
import ru.netology.dto.Post

typealias LikeListener = (Post) -> Unit

class PostAdapter(val likeListener: LikeListener) : RecyclerView.Adapter<PostViewHolder>() {
    var list = emptyList<Post>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, likeListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = list[position]
        holder.bind(post)

    }

    override fun getItemCount(): Int {
        return list.size
    }
}

class PostViewHolder(val binding: CardPostBinding,val likeListener: LikeListener) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
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

            likes.setOnClickListener {
                likeListener(post)
            }

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