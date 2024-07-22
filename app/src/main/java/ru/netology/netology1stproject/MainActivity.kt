package ru.netology.netology1stproject


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.netology1stproject.adapter.PostAdapter
import ru.netology.netology1stproject.adapter.onInteractionListener
import ru.netology.netology1stproject.databinding.ActivityMainBinding
import ru.netology.netology1stproject.dto.Post
import ru.netology.netology1stproject.dto.PostViewModel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val viewModel: PostViewModel by viewModels()


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
                newPostLauncher.launch(post.content)
            }

            override fun playMedia(post: Post) {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(post.video)
                }
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
        }
        )


        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts: List<Post> ->
            val newPost = adapter.currentList.size < posts.size && adapter.currentList.size > 0
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }
        }
    }





