package com.example.ktor.data.network

import android.util.Log
import com.example.ktor.BaseUrl
import com.example.ktor.exception.ApiCallException
import com.example.utils.ApiException
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.features.observer.ResponseObserver
import io.ktor.http.HttpStatusCode
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class KtorClient @Inject constructor() {


    sealed class EndPoint(val url: String) {

        object UserEndPoint : EndPoint("${BaseUrl.BASE_URL}/user}")
    }

    fun getHttpClient() = HttpClient(Android) {
//
//        defaultRequest {
//            contentType(ContentType.Application.Json)
//            contentType(ContentType.Application.FormUrlEncoded)
//        }


        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
            engine {
                connectTimeout = TIME_OUT
                socketTimeout = TIME_OUT
            }
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

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Log.v(TAG_KTOR_LOGGER, message)
                }

            }
            level = LogLevel.ALL
        }
        HttpResponseValidator {
            handleResponseException {
                when (it) {
                    is ClientRequestException -> {
                        val statusCode = it.response.status.value
                        when (statusCode) {
                            HttpStatusCode.NotFound.value -> throw ApiCallException.NotFoundException("Resource not found")
                            HttpStatusCode.Unauthorized.value -> throw ApiCallException.UnauthorizedUserException("Unauthorized")
                            // Add more status code handling as needed
                            else -> throw ApiException("HTTP error: $statusCode")
                        }
                    }

                    is ConnectException -> throw ApiCallException.NetworkException("Failed to connect to the server")
                    is SocketTimeoutException -> throw ApiCallException.TimeoutException("Request timed out")
                    else -> throw ApiCallException.ApiExceptions("Unknown error: ${it.localizedMessage}")
                }
            }
        }

        install(ResponseObserver) {
            onResponse {
                Log.e(TAG_HTTP_STATUS_LOGGER, "getHttpClient: ---------------------------onResponse-status--${it.status}")
            }
        }


    }


    companion object {
        private const val TIME_OUT = 10_000
        private const val TAG_KTOR_LOGGER = "ktor_logger:"
        private const val TAG_HTTP_STATUS_LOGGER = "http_status:"
    }


}
