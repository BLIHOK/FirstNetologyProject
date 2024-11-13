package ru.netology.netology1stproject.activity


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.Constants.TAG
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingRegistrar
import com.google.firebase.messaging.FirebaseMessagingService
import ru.netology.netology1stproject.FCMService
import ru.netology.netology1stproject.activity.NewPostFragment.Companion.textArg
import ru.netology.netology1stproject.NewPostResultContract
import ru.netology.netology1stproject.R
import ru.netology.netology1stproject.adapter.PostAdapter
import ru.netology.netology1stproject.adapter.onInteractionListener
import ru.netology.netology1stproject.databinding.FragmentFeedBinding
import ru.netology.netology1stproject.dto.Post
import ru.netology.netology1stproject.viewmodel.PostViewModel


class FeedFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)


//        val viewModel: PostViewModel by viewModels (
//            ownerProducer = ::requireParentFragment
//        )
        val viewModel: PostViewModel by activityViewModels()


        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.changeContentAndSave(result)
        }


        val adapter = PostAdapter(object : onInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
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
                    }
                )
            }
        }
        )


        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts: List<Post> ->
            val newPost = adapter.currentList.size < posts.size && adapter.currentList.size > 0
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        binding.save.setOnClickListener {
            findNavController().navigate(R.id.newPostFragment)
        }

        viewModel.edited.observe(viewLifecycleOwner) {
            if (it.id != 0L) {
                findNavController().navigate(
                    R.id.action_feedFragment_to_newPostFragment2,
                    Bundle().apply {
                        textArg = it.content
                    })
            }

        }



        return binding.root
    }
}





