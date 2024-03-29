package com.example.ktor.data.model

import com.example.data.model.CommonResponse
import com.example.data.model.ProductModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    @SerialName("id")
    val id: Int,
    @SerialName("imageUrl")
    val imageUrl: String,
    @SerialName("firstName")
    val firstName: String,
    @SerialName("lastName")
    val lastName: String,
    @SerialName("email")
    val email: String,
    @SerialName("contactNumber")
    val contactNumber: String,
    @SerialName("age")
    val age: Int,
    @SerialName("dob")
    val dob: String,
    @SerialName("salary")
    val salary: Float,
    @SerialName("address")
    val address: String,
)


@Serializable
data class UserEntity(
    @SerialName("age")
    val age: Int,
    @SerialName("count")
    val count: Int,
    @SerialName("name")
    val name: String,
)


@Serializable
data class Test(
    var data: ProductModel = ProductModel(),
)

@Serializable
data class Post(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String,
) : CommonResponse()
