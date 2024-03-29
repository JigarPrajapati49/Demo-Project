package com.example.data.model

const val NETWORK_PAGE_SIZE = 25

data class MovieResponse(val page: Int, val results: MutableList<Movie>)