package ru.netology.netology1stproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.netology1stproject.adapter.PostAdapter
import ru.netology.netology1stproject.databinding.ActivityMainBinding
import ru.netology.netology1stproject.dto.Post
import ru.netology.netology1stproject.dto.PostViewModel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val viewModel: PostViewModel by viewModels()
        val adapter = PostAdapter(
            onLike = { viewModel.likeById(it.id) },
            onShare = { viewModel.shareById(it.id) }
        )
        viewModel.data.observe(this) { posts: List<Post> ->
            adapter.submitList(posts)
        }
        binding.root.adapter = adapter

    }

}




