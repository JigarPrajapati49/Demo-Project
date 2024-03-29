package com.example.data.network

import com.example.data.model.CommonResponse
import com.example.utils.ApiException
import com.example.utils.NoInternetException
import com.google.gson.JsonSyntaxException
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class APIRequest {

    private val STATUS_OK = 200
    private val STATUS_SUCCESS = 1
    private val STATUS_UNAUTHORIZED = 401

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        try {
            val response = call.invoke()

            if (response.body() != null && response.isSuccessful && (response.body() as CommonResponse).success == STATUS_SUCCESS) {
                return response.body()!!
            } else {
                val message = StringBuilder()
                if (response.body() != null) {
                    val error = (response.body() as CommonResponse).message
                    if (error.isEmpty()) {
                        message.append("Something went wrong")
                    } else {
                        message.append(error)
                    }
                } else {
                    message.append("Something went wrong")
                }
                throw ApiException(message.toString())
            }
        } catch (e: JsonSyntaxException) {
//            Handle the dataType mis-match here
            e.printStackTrace()
            throw ApiException("Something went wrong")
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            throw ApiException("Something went wrong")
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            throw ApiException("Something went wrong")
        } catch (e: TypeCastException) {
            e.printStackTrace()
            throw ApiException("Something went wrong")
        } catch (e: NoInternetException) {
            e.printStackTrace()
            throw NoInternetException("No internet connection")
        } catch (e: Exception) {
            e.printStackTrace()
            throw ApiException(e.message.toString())
        }
    }
}