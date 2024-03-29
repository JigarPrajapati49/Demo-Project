package com.example.ktor


import android.util.Log
import com.example.data.model.ProductResponce
import com.example.ktor.data.Resource
import com.example.ktor.data.model.Cat
import com.example.ktor.data.model.Movie
import com.example.ktor.data.model.Post
import com.example.ktor.data.network.ApiInterfaceKtor
import com.example.utils.ApiException
import com.example.utils.NoInternetException
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.HttpRequestTimeoutException
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import kotlinx.serialization.Serializable
import java.io.IOException
import javax.inject.Inject


class MoviesRepositoryImpl @Inject constructor(val client: HttpClient) : ApiInterfaceKtor {


    override suspend fun getPopularMovies(): List<Movie> {
        return try {
            client.get<List<Movie>>() {
                url(BaseUrl.BASE_URL)
            }
        } catch (e: NoInternetException) {
            Log.e("TAG", "NoInternetException: -${e.message}-${e.printStackTrace()}")
            return emptyList()
        } catch (e: ApiException) {
            Log.e("TAG", "ApiException: -${e.message}")
            return emptyList()
        } catch (e: Exception) {
            Log.e("TAG", "getPopularMovies: -${e.message}")
            return emptyList()
        }

    }

    override suspend fun getProduct(): ProductResponce {
        return try {
            client.get<ProductResponce> {
                url("https://www.jsonkeeper.com/b/THIG/")
            }
        } catch (cause: HttpRequestTimeoutException) { // If we have any Time out Exception
            Log.d("TAG", "ConnectTimeoutException failure: $cause")
            return ProductResponce()
        } catch (cause: IOException) { // Network failure is handled
            Log.d("TAG", "IOException failure: $cause")
            return ProductResponce()
        } catch (clientException: ClientRequestException) { // If we have client side issue like invalid request (Or) data miss
            val exceptionResponse = clientException.response
            if (exceptionResponse.status == HttpStatusCode.Unauthorized) {
                // un authorized
                Log.d("TAG", "un authorized failure: ")

            } else {
                // Api Error
                Log.d("TAG", "Api Error ")
            }
            return ProductResponce()
        } catch (cause: Exception) { // If other general exception
            Log.d("TAG", "General Exception failure: $cause")
            return ProductResponce()
        }

    }

    override suspend fun getCatData(): List<Cat> {
        return client.get<List<Cat>>() {
            url("https://api.thecatapi.com/v1/images/search")
        }
    }

    override suspend fun login(
    ): RegisterResponse {
//        return client.post<RegisterResponse>() {
//            url("https://api.digital-diary.rayoinnovations.com/v1/users/login")
//            contentType(ContentType.Application.Json)
//            headers {
//                append("language_code", "en")
//            }
//            parameter("email", "Test123@gmail.com")
//            parameter("password", "Jigar@123")
//            parameter("login_type", "EMAIL")
//            parameter("device_id", "1eb0c4ca-92ca-42b5-b63b-35a6eb8d29dc")
//            parameter("device_name", "sdk_gphone64_x86_64")
//            parameter("subscription_id", "2440b933-642f-4b14-85de-d2139ba626c0")
//        }

        // Work Also
//        return client.submitForm<RegisterResponse>("https://api.digital-diary.rayoinnovations.com/v1/users/login",
//            formParameters = Parameters.build {
//                append("email", "Test123@gmail.com")
//                append("password", "Jigar@123")
//                append("login_type", "EMAIL")
//                append("device_id", "1eb0c4ca-92ca-42b5-b63b-35a6eb8d29dc")
//                append("device_name", "sdk_gphone64_x86_64")
//                append("subscription_id", "2440b933-642f-4b14-85de-d2139ba626c0")
//            }) {
//            headers {
//                append("language_code", "en")
//            }
//        }


        val defaultResponse = RegisterResponse() // Your default response or an instance of the expected type

        val result = CommonException.executeHttpRequest(
            client,
            "https://api.digital-diary.rayoinnovations.com/v1/users/login",
            Parameters.build {
                append("email", "Test123@gmail.com")
                append("password", "Jigar@123")
                append("login_type", "EMAIL")
                append("device_id", "1eb0c4ca-92ca-42b5-b63b-35a6eb8d29dc")
                append("device_name", "sdk_gphone64_x86_64")
                append("subscription_id", "2440b933-642f-4b14-85de-d2139ba626c0")
            },
            {
                append("language_code", "en")
            },
            defaultResponse
        )
        return result

        /*return try {
            client.submitForm<RegisterResponse>("https://api.digital-diary.rayoinnovations.com/v1/users/login",
                formParameters = Parameters.build {
                    append("email", "Test123@gmail.com")
                    append("password", "Jigar@123")
                    append("login_type", "EMAIL")
                    append("device_id", "1eb0c4ca-92ca-42b5-b63b-35a6eb8d29dc")
                    append("device_name", "sdk_gphone64_x86_64")
                    append("subscription_id", "2440b933-642f-4b14-85de-d2139ba626c0")
                }) {
                headers {
                    append("language_code", "en")
                }
            }
        } catch (cause: HttpRequestTimeoutException) { // If we have any Time out Exception
            Log.d("TAG", "ConnectTimeoutException failure: $cause")
            return RegisterResponse()
        } catch (cause: IOException) { // Network failure is handled
            Log.d("TAG", "IOException failure: $cause")
            return RegisterResponse()
        } catch (clientException: ClientRequestException) { // If we have client side issue like invalid request (Or) data miss
            val exceptionResponse = clientException.response
            if (exceptionResponse.status == HttpStatusCode.Unauthorized) {
                // un authorized
                Log.d("TAG", "un authorized failure: ")
            } else {
                // Api Error
                Log.d("TAG", "Api Error ")
            }
            return RegisterResponse()
        } catch (cause: Exception) { // If other general exception
            Log.d("TAG", "General Exception failure: $cause")
            return RegisterResponse()
        }*/

        //Work
//        return client.post("https://api.digital-diary.rayoinnovations.com/v1/users/login") {
//            body = FormDataContent(Parameters.build {
//                headers {
//                    append("language_code", "en")
//                }
//                append("email", "Test123@gmail.com")
//                append("password", "Jigar@123")
//                append("login_type", "EMAIL")
//                append("device_id", "1eb0c4ca-92ca-42b5-b63b-35a6eb8d29dc")
//                append("device_name", "sdk_gphone64_x86_64")
//                append("subscription_id", "2440b933-642f-4b14-85de-d2139ba626c0")
//            })
//        }

    }

    override suspend fun getPost(): List<Post> {
        main()
        return client.get("https://jsonplaceholder.typicode.com/posts") {

        }
    }

    override suspend fun getPostWithCommonResponce(): Resource<List<Post>> {
        return try {
            Resource.Success(client.get("https://jsonplaceholder.typicode.com/posts"))
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    suspend inline fun <reified T : Any> executeHttpRequest(
        url: String,
        onSuccess: (T) -> Unit,
        onError: (String) -> Unit,
    ) {
        try {
            val response: HttpResponse = client.get(url)
            when (response.status) {
                HttpStatusCode.OK -> {
                    val result: T = response.receive()
                    onSuccess(result)
                }

                else -> {
                    val errorResponse: ApiResponses = response.receive()
                    onError(errorResponse.message)
                }
            }
        } catch (e: Exception) {
            onError("Exception: ${e.message}")
        } finally {
            client.close()
        }
    }

    data class ApiResponses(val success: Boolean, val message: String)


    suspend fun main() {
        val apiUrl = "https://jsonplaceholder.typicode.com/todos/1"

        executeHttpRequest<Todo>(
            url = apiUrl,
            onSuccess = { todo ->
                println("API Response: $todo")
                Log.e("TAG", "main: =======================${todo}")
            },
            onError = { errorMessage ->
                println("API Error: $errorMessage")
                Log.e("TAG", "main: =======================errorMessage=====${errorMessage}")
            }
        )
    }

    @Serializable
    data class Todo(
        val userId: Int,
        val id: Int,
        val title: String,
        val completed: Boolean,
    )
}
