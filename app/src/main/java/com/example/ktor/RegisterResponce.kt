package com.example.ktor

import com.example.data.model.CommonResponse
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class RegisterResponse : CommonResponse() {
    val data: RegisterData? = null
}

@Serializable
open class RegisterData {
    val token: String? = null
    val email: String? = null

    @SerializedName("contact_number")
    val contactNumber: String? = null

    @SerializedName("country_code")
    val countryCode: String? = null

    @SerializedName("iso_code")
    val isoCode: String? = null

    @SerializedName("business_name")
    val businessName: String? = null

    @SerializedName("device_id")
    val deviceId: String? = null

    @SerializedName("device_name")
    val deviceName: String? = null

    @SerializedName("_id")
    val userId: String? = null

    @SerializedName("currency_symbol")
    val currencySymbol: String? = null

    @SerializedName("currency_country_name")
    val currencyCountryName: String? = null

    @SerializedName("user_type")
    val userType: String? = null

    @SerializedName("login_type")
    val loginType: String? = null
}

@Serializable
class GoogleLoginResponse : CommonResponse() {
    @SerializedName("data")
    var googleLoginData: GoogleLoginData? = null
}

@Serializable
class GoogleLoginData : RegisterData() {
    @SerializedName("is_new_user")
    var isNewUser: Int = 0
}