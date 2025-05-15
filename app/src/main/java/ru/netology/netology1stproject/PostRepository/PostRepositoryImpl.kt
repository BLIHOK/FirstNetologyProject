package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import ru.netology.netology1stproject.dto.Post
import java.io.IOException
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

    override fun getAllAsync(callback: PostRepository.GetAllCallback) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

//        println("1 ${Thread.currentThread().name}}")
        client.newCall(request)
            .enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
//                        println("2 ${Thread.currentThread().name}}")
                        val posts =
                            response.body?.string() ?: throw RuntimeException("body is null")
                        callback.onSuccess(gson.fromJson(posts, typeToken.type))
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }
            })
//        println("3 ${Thread.currentThread().name}}")
    }


    override fun likeByIdAsync(id: Long, callback: PostRepository.SingleOperationCallback) {
        val request = Request.Builder()
            .post("".toRequestBody())
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        callback.onSuccess()
                    } else {
                        callback.onError(IOException("Ошибка: код ${response.code}"))
                    }
                } catch (e: Exception) {
                    callback.onError(e)
                } finally {
                    response.close()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }

    override fun unlikeByIdAsync(id: Long, callback: PostRepository.SingleOperationCallback) {
        val request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id/likes")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        callback.onSuccess()
                    } else {
                        callback.onError(IOException("Ошибка: код ${response.code}"))
                    }
                } catch (e: Exception) {
                    callback.onError(e)
                } finally {
                    response.close()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }

    override fun shareByIdAsync(id: Long, callback: PostRepository.SingleOperationCallback) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        callback.onSuccess()
                    } else {
                        callback.onError(IOException("Ошибка: код ${response.code}"))
                    }
                } catch (e: Exception) {
                    callback.onError(e)
                } finally {
                    response.close()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }

    override fun saveAsync(post: Post, callback: PostRepository.SingleOperationCallback) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/posts")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        callback.onSuccess()
                    } else {
                        callback.onError(IOException("Ошибка: код ${response.code}"))
                    }
                } catch (e: Exception) {
                    callback.onError(e)
                } finally {
                    response.close()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }

    override fun removeByIdAsync(id: Long, callback: PostRepository.SingleOperationCallback) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/posts/$id")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                try {
                    if (response.isSuccessful) {
                        callback.onSuccess()
                    } else {
                        callback.onError(IOException("Ошибка: код ${response.code}"))
                    }
                } catch (e: Exception) {
                    callback.onError(e)
                } finally {
                    response.close()
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }
}