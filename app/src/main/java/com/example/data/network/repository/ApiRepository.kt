package com.example.data.network.repository

import com.example.data.model.Product
import com.example.data.model.ProductResponce
import com.example.data.network.APIRequest
import com.example.data.network.ApiInterface
import com.example.db.ProductDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(
    private val client: ApiInterface,
    private val productDatabase: ProductDatabase,
) : APIRequest() {

    suspend fun getDataFromApi(): ProductResponce = apiRequest { client.getProduct() }

    suspend fun getProductFromDatabase(): List<Product> {
        return productDatabase.productDao().getProduct()
    }

    suspend fun updateProductFromDatabase(shadow: Product) {
        return productDatabase.productDao().updateProduct(shadow)
    }

    suspend fun updateData(shadow: Product): ProductResponce = apiRequest { client.getProduct() }


}
