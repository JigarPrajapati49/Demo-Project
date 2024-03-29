package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponce(var data: ProductModel= ProductModel()) : CommonResponse() {

}
@Serializable
class ProductModel {
    @SerializedName("details")
    var productList : MutableList<Product> = ArrayList()
}

@Serializable
@Entity(tableName = "product_table")
class Product {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @SerializedName("image")
    var image: String? = null

    @SerializedName("description")
    var description: String? = null

    @SerializedName("shadow")
    var shadow: Int = 0
}



