package ru.netology.netology1stproject.utils

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import ru.netology.netology1stproject.NewPostFragment.Companion.textArg
import ru.netology.netology1stproject.NewPostResultContract
import ru.netology.netology1stproject.R
import ru.netology.netology1stproject.adapter.PostViewHolder
import ru.netology.netology1stproject.adapter.onInteractionListener
import ru.netology.netology1stproject.databinding.FragmentOnePostBinding
import ru.netology.netology1stproject.dto.Post
import ru.netology.netology1stproject.dto.PostViewModel

class OnePostFragment : Fragment() {

    //    val viewModel: PostViewModel by viewModels(
//        ownerProducer = ::requireParentFragment
//    )
    val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentOnePostBinding.inflate(inflater, container, false)


        val viewHolder = PostViewHolder(binding.post, object : onInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }

                val shareIntent =
                    Intent.createChooser(intent, getString(R.string.choose_share_post))
                startActivity(shareIntent)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)

            }

            override fun playMedia(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(post.video)
                }
                startActivity(intent)

            }

            override fun onOpen(post: Post) {
                findNavController().currentDestination //nothing to do
            }
        })


        viewModel.edited.observe(viewLifecycleOwner) {
            if (it.id != 0L) {
                findNavController().navigate(
                    R.id.action_onePostFragment_to_newPostFragment2,
                    Bundle().apply {
                        textArg = it.content
                    })
            }
        }

        val id = arguments?.textArg?.toLong() ?: -1
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val post = posts.find { it.id == id } ?: run {
                findNavController().navigateUp()
                return@observe
            }
            viewHolder.bind(post)
        }



        return binding.root
    }
}