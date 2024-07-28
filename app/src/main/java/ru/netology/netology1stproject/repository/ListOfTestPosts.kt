package ru.netology.netology1stproject.repository

import ru.netology.netology1stproject.dto.Post
private var nextId = 1L

class ListOfTestPosts {
    var posts = listOf(
    Post(
    id = nextId++,
    author = "Нетология. Университет интернет-профессий будущего",
    content = "Привет, это новая Нетология! Это дополнительный пост для массива постов",
    published = "21 мая в 18:36",
    likeCount = 999,
    likedByMe = false,
    shareCount = 10000,
    shareByMe = false,
    watchCount = 550,
    video = null
    ),
    Post(
    id = nextId++,
    author = "Нетология. Университет интернет-профессий будущего",
    content = "Привет, это новая Нетология! Это дополнительный пост для массива постов",
    published = "21 мая в 18:36",
    likeCount = 9999999,
    likedByMe = false,
    shareCount = 7,
    shareByMe = false,
    watchCount = 178359,
    video = null
    ),
    Post(
    id = nextId++,
    author = "Нетология. Университет интернет-профессий будущего",
    content = "Привет, это новая Нетология!",
    published = "21 мая в 18:36",
    likeCount = 100,
    likedByMe = false,
    shareCount = 999,
    shareByMe = false,
    watchCount = 55547,
    video = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
    ),
    ).reversed()
}