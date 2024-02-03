package ru.netology.netology1stproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.netology1stproject.R
import ru.netology.netology1stproject.databinding.PostCardBinding
import ru.netology.netology1stproject.dto.Post
import ru.netology.netology1stproject.utils.AndroidUtils


interface onInteractionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
}

class PostAdapter(
    private val onInteractionListener: onInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = PostCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(view, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}


class PostViewHolder(
    private val binding: PostCardBinding,
    private val onInteractionListener: onInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likesCounter.text = AndroidUtils.changeCountersImagin(post.likeCount)
            sharesCounter.text = AndroidUtils.changeCountersImagin(post.shareCount)
            watchesCounter.text = AndroidUtils.changeCountersImagin(post.watchCount)
            likes.setImageResource(
                if (post.likedByMe) R.drawable.ic_redhearth_foreground else R.drawable.ic_hearth_foreground
            )

            likes.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            shares.setOnClickListener {
                onInteractionListener.onShare(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }

                            R.id.edit -> {
                                onInteractionListener.onEdit(post)


                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

        }

        binding.author.setOnClickListener {
            ////сворачивает текст
            binding.author.isSingleLine = !binding.author.isSingleLine

        }

    }
}


object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Post, newItem: Post) =
//        oldItem.content == newItem.content
        oldItem == newItem

}