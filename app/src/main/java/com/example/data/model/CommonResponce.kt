package com.example.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
open class CommonResponse(
    val success: Int = 0,
    val message: String = "",
    val status: Int = 0
)