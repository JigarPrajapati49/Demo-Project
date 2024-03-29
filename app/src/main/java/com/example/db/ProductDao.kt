package com.example.db

import androidx.room.*
import com.example.data.model.Product


@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addProduct(products: List<Product>)

    @Query("SELECT * FROM product_table")
    suspend fun getProduct(): List<Product>

    @Update
    suspend fun updateProduct(product: Product)
}