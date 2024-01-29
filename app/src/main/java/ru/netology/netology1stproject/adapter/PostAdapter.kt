package ru.netology.netology1stproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.netology1stproject.R
import ru.netology.netology1stproject.databinding.ActivityMainBinding
import ru.netology.netology1stproject.databinding.PostCardBinding
import ru.netology.netology1stproject.dto.Post


typealias onLikeListener = (post: Post) -> Unit
typealias onShareListener = (post: Post) -> Unit

class PostAdapter(
    private val onLike: onLikeListener,
    private val onShare: onShareListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(view, onLike, onShare)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class PostViewHolder(
    private val binding: PostCardBinding,
    private val onLike: onLikeListener,
    private val onShare: onShareListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
//            likesCounter.text = changeCountersImagin(post.likeCount)
//            sharesCounter.text = changeCountersImagin(post.shareCount)
            likesCounter.text = post.likeCount.toString()
            sharesCounter.text = post.shareCount.toString()
            likes.setImageResource(
                if (post.likedByMe) R.drawable.ic_redhearth_foreground else R.drawable.ic_hearth_foreground
            )

            likes.setOnClickListener {
                onLike(post)
            }

//            shares.setOnClickListener {
//                onShare(post)
//            }
        }
    }
}


object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) =
        oldItem.content == newItem.content

}