package com.example.ktor

import com.example.ktor.data.model.Post

interface CollectionsService {
    suspend fun getPost(): List<Post>
}