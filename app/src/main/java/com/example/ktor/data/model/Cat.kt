package com.example.ktor.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Cat(
    val height: Int,
    val id: String,
    val url: String,
    val width: Int,
)