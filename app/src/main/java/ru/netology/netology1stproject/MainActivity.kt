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

        //        binding.author?.setOnClickListener {
//            ////сворачивает текст
//            binding.author.isSingleLine = !binding.author.isSingleLine
//        }
    }


//    private fun changeCountersImagin(counter: Int): String {
//
//        return when (counter) {
//            in 0..999 -> counter.toString()
//            in 1000..9999 ->
//                if ((counter % 1000) == 0) {
//                    (counter / 1000).toString() + "K"
//                } else if (counter % 1000 < 100) {
//                    (counter / 1000).toString() + "K"
//                } else (counter / 1000).toString() + "." + ((counter / 100) % 10).toString() + "K"
//
//            in 10000..999999 -> (counter / 1000).toString() + "K"
//
//            in 1000000..10000000 ->
//                if ((counter % 1000000) == 0) {
//                    (counter / 1000000).toString() + "M"
//                } else if (counter % 1000000 < 100000) {
//                    (counter / 1000000).toString() + "M"
//                } else (counter / 1000000).toString() + "." + ((counter / 100000) % 10).toString() + "M"
//
//            else -> "0"
//        }
//    }
}




