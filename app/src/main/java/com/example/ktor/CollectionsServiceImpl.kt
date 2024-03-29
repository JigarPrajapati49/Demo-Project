package com.example.ktor

import android.util.Log
import com.example.ktor.data.model.Post
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.HttpRequestTimeoutException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class CollectionsServiceImpl @Inject constructor(
    private val httpClient: HttpClient,
) : CollectionsService {


    override suspend fun getPost(): List<Post> {
        try {
            return httpClient.get("https://jsonplaceholder.typicode.com/posts") {
            }
            //makes the assumption that you have already handled IO, SocketTimeout and UnknownHost exceptions
        } catch (exception: ResponseException) {

            when (exception) {
                is RedirectResponseException -> {
                    // Status codes 3XX
                    val errorString = exception.response.receive<String>()

                    val jsonObject = JSONObject(errorString)

                    val errorMessage = jsonObject.getString("status_message")

                    // pass it wherever you would like to
                }

                is ClientRequestException -> {
                    // status codes 4XX
                }

                is ServerResponseException -> {
                    // status codes 5XX
                }
            }

            return emptyList()

        } catch (timeOutException: HttpRequestTimeoutException) {

            Log.e("TAG", "getPost: -----------------HttpRequestTimeoutException Message--${timeOutException.message}")
            Log.e("TAG", "getPost: -------------------${timeOutException.printStackTrace()}")
            return emptyList()
        } catch (networkException: IOException) {
            Log.d("TAG", "NO InternetConnection  failure: ${networkException.message}")
            return emptyList()
        } catch (clientException: ClientRequestException) { // If we have client side issue like invalid request (Or) data miss
            val exceptionResponse = clientException.response
            if (exceptionResponse.status == HttpStatusCode.Unauthorized) {
                // un authorized
                Log.d("TAG", "un authorized failure: ")
            } else {
                // Api Error
                Log.d("TAG", "Api Error ")
            }
            return emptyList()
        }
    }

}