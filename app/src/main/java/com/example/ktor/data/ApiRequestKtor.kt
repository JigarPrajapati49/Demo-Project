package com.example.ktor.data

import com.example.ktor.MoviesRepositoryImpl
import com.example.ktor.exception.ApiCallException
import com.google.gson.JsonSyntaxException
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.HttpRequestTimeoutException
import io.ktor.client.features.HttpResponseValidator
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


open class ApiService(val httpClient: HttpClient/*, private val context: Context*/) {
    companion object {
        const val STATUS_SUCCESS = 1
    }

    suspend fun <T> apiCall(call: suspend () -> T): T {
        return try {
            call.invoke()
        } catch (e: ClientRequestException) {
            throw ApiCallException.ApiExceptions("Client-side error: ${e.response.status.value}")
        } catch (e: ServerResponseException) {
            throw ApiCallException.ApiExceptions("Server-side error: ${e.response.status.value}")
        } catch (e: HttpRequestTimeoutException) {
            throw ApiCallException.TimeoutException("Request timeout")
        } catch (e: ApiCallException.NetworkException) {
            throw ApiCallException.NetworkException("Network error")
        } catch (e: Exception) {
            throw ApiCallException.ApiExceptions("Unexpected error: ${e.message}")
        }
    }


    suspend inline fun <reified T : Any> apiRequestWithErrorWhileRetrofit(call: suspend () -> HttpResponse): T {
        try {
            val response = call.invoke()

            if (response.status == HttpStatusCode.OK) {
                val body = response.receive<T>()
                if (body is List<*>) {
                    return body
                } else {
                    showToast("Expected response type is not a List")
                    throw ApiCallException.ApiExceptions("Expected response type is not a List")
                }
            } else {
                showToast("Something went wrong. Status: ${response.status}")
                throw ApiCallException.ApiExceptions("Something went wrong. Status: ${response.status}")
            }
        } catch (e: SocketTimeoutException) {
            showToast("Request timeout")
            throw ApiCallException.ApiExceptions("Request timeout")
        } catch (e: ApiCallException.NetworkException) {
            showToast("No internet connection")
            throw ApiCallException.NetworkException("No internet connection")
        } catch (e: JsonSyntaxException) {
            // Handle the dataType mis-match here
            showToast("Something went wrong")
            throw ApiCallException.ApiExceptions("Something went wrong")
        } catch (e: Exception) {
            showToast("Something went wrong")
            throw ApiCallException.ApiExceptions(e.message ?: "Something went wrong")
        }
    }

    suspend inline fun <reified T : Any> apiRequest(call: () -> HttpResponse): T {
        try {
            val response = call.invoke()

            if (response.status == HttpStatusCode.OK) {
                val body = response.receive<T>()
                if (body is CommonResponse && body.success == STATUS_SUCCESS) {
                    return body
                } else if (body is CommonResponse && body.status == 401) {
                    throw ApiCallException.UnauthorizedUserException(
                        (body as CommonResponse).message
                            ?: "Unauthorized user"
                    )
                } else {
                    val m = StringBuilder()
                    val body = response.receive<T>()
                    if (body != null) {
                        val error = (body as com.example.data.model.CommonResponse).message
                        if (error.isEmpty()) {
                            m.append("Something went wrong")
                        } else {
                            m.append(error)
                        }
                    } else {
                        m.append("Something went wrong")
                    }
                    throw ApiCallException.ApiExceptions(m.toString())
                }
            } else {
                throw ApiCallException.ApiExceptions("Something went wrong. Status: ${response.status}")
            }
        } catch (e: SocketTimeoutException) {
            throw ApiCallException.ApiExceptions("Request timeout")
        } catch (e: UnknownHostException) {
            throw ApiCallException.ApiExceptions("No internet connection")
        } catch (e: JsonSyntaxException) {
            // Handle the dataType mis-match here
            throw ApiCallException.ApiExceptions("Something went wrong")
        } catch (e: Exception) {
            throw ApiCallException.ApiExceptions(e.message ?: "Something went wrong")
        }
    }

    suspend inline fun <reified T : Any> executeHttpRequest(
        url: String,
        onSuccess: (T) -> HttpResponse,
        onError: (String) -> HttpResponse,
    ) {
        try {
            val response: HttpResponse = httpClient.get(url)
            when (response.status) {
                HttpStatusCode.OK -> {
                    val result: T = response.receive()
                    onSuccess(result)
                }

                else -> {
                    val errorResponse: MoviesRepositoryImpl.ApiResponses = response.receive()
                    onError(errorResponse.message)
                }
            }
            if (response.status == HttpStatusCode.OK) {
                val body = response.receive<T>()
                if (body is CommonResponse && body.success == STATUS_SUCCESS) {
                    return body.success as Unit
                } else {
                    val m = StringBuilder()
                    val body = response.receive<T>()
                    if (body != null) {
                        val error = (body as com.example.data.model.CommonResponse).message
                        if (error.isEmpty()) {
                            m.append("Something went wrong")
                        } else {
                            m.append(error)
                        }
                    } else {
                        m.append("Something went wrong")
                    }
                    throw ApiCallException.ApiExceptions(m.toString())
                }
            } else {
                throw ApiCallException.ApiExceptions("Something went wrong. Status: ${response.status}")
            }
        } catch (e: Exception) {
            onError("Exception: ${e.message}")
        } finally {
            httpClient.close()
        }
    }


    suspend inline fun <reified T> makeApiCall(
        httpClient: HttpClient,
        url: String,
        method: HttpMethod = HttpMethod.Get,
        headers: Map<String, String>? = null,
        body: Any? = null,
    ): T {
        return httpClient.request {
            this.url(url)
            this.method = method

            headers?.let {
                for ((key, value) in it) {
                    this.headers.append(key, value)
                }
            }

            body?.let {
                this.body = it
            }
        }
    }

    fun showToast(message: String) {
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

data class CommonResponse(val success: Int, val message: String?, val status: Int = 0)

sealed class Resource<out R> {
    data class Success<out R>(val result: R) : Resource<R>()
    data class Failure(val exception: Exception) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}

class ApiClient(private val baseUrl: String) {

    private val client = HttpClient {


        HttpResponseValidator {
            handleResponseException {
                when (it) {
                    is ClientRequestException -> {
                        val statusCode = it.response.status.value
                        when (statusCode) {
                            HttpStatusCode.NotFound.value -> throw ApiCallException.NotFoundException("Resource not found")
                            HttpStatusCode.Unauthorized.value -> throw ApiCallException.UnauthorizedUserException("Unauthorized")
                            // Add more status code handling as needed
                            else -> throw ApiCallException.ApiExceptions("HTTP error: $statusCode")
                        }
                    }

                    is ConnectException -> throw ApiCallException.NetworkException("Failed to connect to the server")
                    is SocketTimeoutException -> throw ApiCallException.TimeoutException("Request timed out")
                    else -> throw ApiCallException.ApiExceptions("Unknown error: ${it.localizedMessage}")
                }
            }
        }
    }


    // Custom exceptions
//    class ApiException(message: String) : Exception(message)
//    class NotFoundException(message: String) : ApiException(message)
//    class UnauthorizedException(message: String) : ApiException(message)
//    class NetworkException(message: String) : Exception(message)
//    class TimeoutException(message: String) : Exception(message)
}

