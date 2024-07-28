package ru.netology.netology1stproject.adapter


import android.view.LayoutInflater
import android.view.View
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
    fun playMedia(post: Post)
    fun onOpen(post: Post)
}

class PostAdapter(
    private val onInteractionListener: onInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostViewHolder.PostDiffCallback) {

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
            likes.text = AndroidUtils.changeCountersImagin(post.likeCount)
            shares.text = AndroidUtils.changeCountersImagin(post.shareCount)
            watchesCounter.text = AndroidUtils.changeCountersImagin(post.watchCount)
            likes.isChecked = post.likedByMe
            likes.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            shares.isChecked = post.shareByMe

            shares.setOnClickListener {
                onInteractionListener.onShare(post)
            }

            if (post.video == null) videoContent.visibility = View.GONE
            if (post.video != null) videoContent.visibility = View.VISIBLE

            videoContent.setOnClickListener {
                onInteractionListener.playMedia(post)
            }


            menu.setOnClickListener {
                menu.isChecked = true
                val popupMenu = PopupMenu(it.context, it).apply {
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
                }
                popupMenu.show()
                popupMenu.setOnDismissListener {
                    binding.menu.isChecked = false
                }
            }

            root.setOnClickListener {
                onInteractionListener.onOpen(post)
            }

        }

        binding.author.setOnClickListener {
            ////сворачивает текст
            binding.author.isSingleLine = !binding.author.isSingleLine
        }


    }


    object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem

    }


}