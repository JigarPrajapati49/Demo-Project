package com.example.data.network

import com.example.data.model.ProductResponce
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET(ApiEndPoints.PRODUCT)
    suspend fun getProduct(): Response<ProductResponce>

}