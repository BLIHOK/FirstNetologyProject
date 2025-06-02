package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.netology1stproject.api.PostsApi
import ru.netology.netology1stproject.dto.Post
import java.util.concurrent.TimeUnit


class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAllAsync(callback: PostRepository.GetAllCallback<List<Post>>) {
        PostsApi.retrofitService.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                } else {

                    callback.onError(RuntimeException("Error code: ${response.code()}"))
                    return
                }
            }

            override fun onFailure(call: Call<List<Post>>, e: Throwable) {
                callback.onError(Exception(e))
            }
        }
        )
    }

    override fun likeByIdAsync(id: Long, callback: PostRepository.GetAllCallback<Post>) {
        PostsApi.retrofitService.likeById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
//                    val body = response.body() ?: throw RuntimeException("body is null")
                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                } else {
                    callback.onError(RuntimeException("${response.code()} : ${response.message()}"))
                    return
                }
            }

            override fun onFailure(call: Call<Post>, e: Throwable) {
                callback.onError(Exception(e))
            }
        }
        )
//        val request = Request.Builder()
//            .post("".toRequestBody())
//            .url("${BASE_URL}/api/posts/$id/likes")
//            .build()

//        client.newCall(request).enqueue(object : Callback {
//            override fun onResponse(call: Call, response: Response) {
//                try {
//                    val post = response.body?.string() ?: throw RuntimeException("body is null")
//                    if (response.isSuccessful) {
//                        callback.onSuccess(gson.fromJson(post, Post::class.java))
//                    } else {
//                        callback.onError(IOException("Ошибка: код ${response.code}"))
//                    }
//                } catch (e: Exception) {
//                    callback.onError(e)
//                } finally {
//                    response.close()
//                }
//            }
//
//            override fun onFailure(call: Call, e: IOException) {
//                callback.onError(e)
//            }
//        })
    }

    override fun unlikeByIdAsync(id: Long, callback: PostRepository.GetAllCallback<Post>) {
        PostsApi.retrofitService.dislikeById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (response.isSuccessful) {
//                    val body = response.body() ?: throw RuntimeException("body is null")
                    callback.onSuccess(response.body() ?: throw RuntimeException("body is null"))
                } else {
                    callback.onError(RuntimeException("${response.code()} : ${response.message()}"))
                    return
                }
            }

            override fun onFailure(call: Call<Post>, e: Throwable) {
                callback.onError(Exception(e))
            }
        }
        )
    }

    override fun shareByIdAsync(id: Long, callback: PostRepository.GetAllCallback<Unit>) {
        PostsApi.retrofitService.shareById(id)
            .enqueue(object : Callback<Unit> { // Использую метод Retrofit GET для post
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
//                    val body = response.body() ?: throw RuntimeException("body is null")
                        callback.onSuccess(
                            response.body() ?: throw RuntimeException("body is null")
                        )
                    } else {
                        callback.onError(RuntimeException("${response.code()} : ${response.message()}"))
                        return
                    }
                }

                override fun onFailure(call: Call<Unit>, e: Throwable) {
                    callback.onError(Exception(e))
                }
            }
            )
    }

    override fun saveAsync(post: Post, callback: PostRepository.GetAllCallback<Post>) {
        PostsApi.retrofitService.save(post)
            .enqueue(object : Callback<Post> { // Использую метод Retrofit GET для post
                override fun onResponse(call: Call<Post>, response: Response<Post>) {
                    if (response.isSuccessful) {
//                    val body = response.body() ?: throw RuntimeException("body is null")
                        callback.onSuccess(
                            response.body() ?: throw RuntimeException("body is null")
                        )
                    } else {
                        callback.onError(RuntimeException("${response.code()} : ${response.message()}"))
                        return
                    }
                }

                override fun onFailure(call: Call<Post>, e: Throwable) {
                    callback.onError(Exception(e))
                }
            }
            )
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.GetAllCallback<Unit>) {
        PostsApi.retrofitService.removeById(id)
            .enqueue(object : Callback<Unit> { // Использую метод Retrofit GET для post
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (response.isSuccessful) {
//                    val body = response.body() ?: throw RuntimeException("body is null")
                        callback.onSuccess(
                            response.body() ?: throw RuntimeException("body is null")
                        )
                    } else {
                        callback.onError(RuntimeException("${response.code()} : ${response.message()}"))
                        return
                    }
                }

                override fun onFailure(call: Call<Unit>, e: Throwable) {
                    callback.onError(Exception(e))
                }
            }
            )
    }
}