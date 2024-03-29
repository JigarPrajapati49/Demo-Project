package com.example.ktor

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.android.Android
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.HttpRequestTimeoutException
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.IOException
import java.util.concurrent.TimeUnit

object ApiCall {

    suspend inline fun <reified T : Any> executeApiCall(
        url: String,
        client: HttpClient = HttpClient(Android) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
                install(HttpTimeout) {
                    socketTimeoutMillis = 30_000
                    requestTimeoutMillis = 30_000
                    connectTimeoutMillis = 30_000
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = TimeUnit.SECONDS.toMillis(20)
                    socketTimeoutMillis = TimeUnit.SECONDS.toMillis(20)
                    connectTimeoutMillis = TimeUnit.SECONDS.toMillis(20)
                }
            }
        },
        crossinline onSuccess: (T) -> Unit,
        crossinline onError: (String) -> Unit,
    ) {
        try {
            withContext(Dispatchers.IO) {
                val response: HttpResponse = client.get(url)
                when (response.status) {
                    HttpStatusCode.OK -> {
                        val result: T = response.receive()
                        onSuccess(result)
                    }

                    else -> {
                        val errorResponse: ApiResponse = response.receive()
                        onError(errorResponse.message)
                    }
                }
            }
        } catch (e: Exception) {
            handleException(e, onError)
        } finally {
            client.close()
        }
    }

    inline fun handleException(exception: Exception, crossinline onError: (String) -> Unit) {
        when (exception) {
            is HttpRequestTimeoutException -> {
                onError("Request Timeout")
                Log.e("TAG", "Request Timeout: $exception")
            }

            is ClientRequestException -> {
                val statusCode = exception.response.status
                when (statusCode) {
                    HttpStatusCode.Unauthorized -> {
                        onError("Unauthorized")
                        Log.e("TAG", "Unauthorized: $exception")
                    }

                    else -> {
                        onError("Api Error: $statusCode")
                        Log.e("TAG", "Api Error: $exception")
                    }
                }
            }

            is IOException -> {
                onError("Network Failure")
                Log.e("TAG", "Network Failure: $exception")
            }

            else -> {
                onError("Unknown Error")
                Log.e("TAG", "Unknown Error: $exception")
            }
        }
    }

    @Serializable
    data class ApiResponse(val success: Boolean, val message: String)
}