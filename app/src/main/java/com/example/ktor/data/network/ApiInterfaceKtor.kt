package com.example.ktor.data.network

import com.example.data.model.ProductResponce
import com.example.ktor.RegisterResponse
import com.example.ktor.data.Resource
import com.example.ktor.data.model.Cat
import com.example.ktor.data.model.Movie
import com.example.ktor.data.model.Post

interface ApiInterfaceKtor {
    suspend fun getPopularMovies(): List<Movie>

    suspend fun getProduct(): ProductResponce

    suspend fun getCatData(): List<Cat>

    suspend fun login(
    ): RegisterResponse

    suspend fun getPost(): List<Post>

    suspend fun getPostWithCommonResponce(): Resource<List<Post>>
}