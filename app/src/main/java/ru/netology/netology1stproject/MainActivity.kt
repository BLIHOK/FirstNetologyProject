package ru.netology.netology1stproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.netology1stproject.databinding.ActivityMainBinding
import ru.netology.netology1stproject.dto.Post


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.author.setOnClickListener {
            ////сворачивает текст
            binding.author.isSingleLine = !binding.author.isSingleLine
        }

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология!",
            published = "21 мая в 18:36",
            likeCount = 1000,
            likedByMe = false,
            shareCount = 999999,
            shareByMe = false
        )

        makeLikeAndShare(binding, post)
    }

    private fun makeLikeAndShare(binding: ActivityMainBinding, post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            likesCounter.text = changeCountersImagin(post.likeCount)
            sharesCounter.text = changeCountersImagin(post.shareCount)

            if (post.likedByMe) {
                likes.setImageResource(R.drawable.ic_redhearth_foreground)
            }
            likes.setOnClickListener {
                if (post.likedByMe) post.likeCount-- else post.likeCount++
                post.likedByMe = !post.likedByMe
                likes.setImageResource(
                    if (post.likedByMe) R.drawable.ic_redhearth_foreground else R.drawable.ic_hearth_foreground
                )
                likesCounter.text = changeCountersImagin(post.likeCount)
            }

            shares.setOnClickListener {
                post.shareCount++
                sharesCounter.text = changeCountersImagin(post.shareCount)
            }
        }
    }

    private fun changeCountersImagin(counter: Int): String {


        return when (counter) {
            in 0..999 -> counter.toString()
            in 1000..9999 ->
                if ((counter % 1000) == 0) {
                    (counter / 1000).toString() + "K"
                } else if (counter % 1000 < 100) {
                    (counter / 1000).toString() + "K"
                } else (counter / 1000).toString() + "." + ((counter / 100) % 10).toString() + "K"


            in 10000..999999 -> (counter / 1000).toString() + "K"


            in 1000000..10000000 ->
                if ((counter % 1000000) == 0) {
                    (counter / 1000000).toString() + "M"
                } else if (counter % 1000000 < 100000) {
                    (counter / 1000000).toString() + "M"
                } else (counter / 1000000).toString() + "." + ((counter / 100000) % 10).toString() + "M"

            else -> "0"
        }
    }
}
