package com.example.ktor

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.HttpRequestTimeoutException
import io.ktor.client.request.forms.submitForm
import io.ktor.http.HeadersBuilder
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import java.io.IOException

object CommonException {
    suspend inline fun <reified T : Any> executeHttpRequest(
        client: HttpClient,
        url: String,
        formParameters: Parameters,
        headerss: HeadersBuilder.() -> Unit = {},
        defaultResponse: T,
    ): T {
        return try {
            client.submitForm<T>(url = url, formParameters = formParameters) {
                headerss(headers)
            }
        } catch (cause: HttpRequestTimeoutException) {
            handleException("ConnectTimeoutException", cause)
            defaultResponse
        } catch (cause: IOException) {
            handleException("IOException", cause)
            defaultResponse
        } catch (clientException: ClientRequestException) {
            handleClientRequestException(clientException)
            defaultResponse
        } catch (cause: Exception) {
            handleException("General Exception", cause)
            defaultResponse
        }
    }

    fun handleException(type: String, cause: Exception) {
        Log.e("TAG", "$type failure: $cause")
    }

    fun handleClientRequestException(clientException: ClientRequestException) {
        val exceptionResponse = clientException.response
        if (exceptionResponse.status == HttpStatusCode.Unauthorized) {
            Log.e("TAG", "Unauthorized failure")
        } else {
            Log.e("TAG", "Api Error")
        }
    }
}