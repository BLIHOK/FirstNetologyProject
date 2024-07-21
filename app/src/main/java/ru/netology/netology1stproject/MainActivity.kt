package ru.netology.netology1stproject


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import ru.netology.netology1stproject.adapter.PostAdapter
import ru.netology.netology1stproject.adapter.onInteractionListener
import ru.netology.netology1stproject.databinding.ActivityMainBinding
import ru.netology.netology1stproject.dto.Post
import ru.netology.netology1stproject.dto.PostViewModel
import ru.netology.netology1stproject.utils.AndroidUtils
import ru.netology.netology1stproject.utils.AndroidUtils.focusAndShowKeyboard


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

        binding.save.setOnClickListener {
            val text = binding.edit.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(this, R.string.error_empty_content, Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            viewModel.changeContentAndSave(text)

            binding.edit.setText("")
            binding.edit.clearFocus()
            AndroidUtils.HideKeyboard(it)

            binding.bannerGroup.visibility = View.GONE
        }

        viewModel.edited.observe(this) { post ->
            if (post.id != 0L) {
                binding.bannerGroup.visibility = View.VISIBLE
                binding.edit.setText(post.content)
                binding.edit.focusAndShowKeyboard()

            }

        }

        binding.closeButton.setOnClickListener {
            val text = binding.edit.text.toString().trim()
            viewModel.changeContentAndSave(text)
            binding.edit.setText("")
            binding.edit.clearFocus()
            AndroidUtils.HideKeyboard(it)
            binding.bannerGroup.visibility = View.GONE
            return@setOnClickListener
        }


        }

    }





