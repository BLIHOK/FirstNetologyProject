package ru.netology.netology1stproject.activity


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.netology1stproject.R
import ru.netology.netology1stproject.activity.NewPostFragment.Companion.textArg
import ru.netology.netology1stproject.adapter.PostAdapter
import ru.netology.netology1stproject.adapter.onInteractionListener
import ru.netology.netology1stproject.databinding.FragmentFeedBinding
import ru.netology.netology1stproject.dto.Post
import ru.netology.netology1stproject.viewmodel.PostViewModel


class FeedFragment : Fragment() {

    val viewModel: PostViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        val adapter = PostAdapter(object : onInteractionListener {

            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun unLike(post: Post) {
                viewModel.unlikeById(post.id)
            }

            override fun onShare(post: Post) {
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                    viewModel.shareById(post.id)
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
                findNavController().navigate(
                    R.id.action_feedFragment_to_onePostFragment, Bundle().apply {
                        textArg = post.id.toString()
                    })
            }
        })

        with(binding) {
            list.adapter = adapter
            viewModel.data.observe(viewLifecycleOwner) { state ->
                adapter.submitList(state.posts)
                progress?.isVisible = state.loading
                errorGroup?.isVisible = state.error
                emptyText?.isVisible = state.empty
            }

            retryButton?.setOnClickListener {
                viewModel.loadPosts()
            }

            save.setOnClickListener {
                findNavController().navigate(R.id.newPostFragment)
            }
        }

        viewModel.edited.observe(viewLifecycleOwner) {
            if (it.id != 0L) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment2, Bundle().apply {
                        textArg = it.content
                    })
            }
        }

        binding.swiperefresh?.setOnRefreshListener {
            viewModel.loadPosts()

            // Hide swipe to refresh icon animation
            binding.swiperefresh.isRefreshing = false
        }


        return binding.root
    }
}





