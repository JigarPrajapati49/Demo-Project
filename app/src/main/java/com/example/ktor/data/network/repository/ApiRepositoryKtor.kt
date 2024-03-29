package com.example.ktor.data.network.repository

import com.example.ktor.data.model.Post
import com.example.ktor.RegisterResponse
import com.example.ktor.data.ApiService
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.Parameters
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ApiRepositoryKtor @Inject constructor(private val apiService: ApiService, private val client: HttpClient) : ApiService(client) {

//    suspend fun fetchData(): List<Post> {
//        return apiService.fetchData()
//    }


    //    suspend fun fetchData(): List<Post> {
//        return apiRequestWithErrorWhileRetrofit {
//            client.get("https://jsonplaceholder.typicode.com/posts")
//        }
//    }
    suspend fun fetchData(): List<Post> = apiCall {
        client.get("https://jsonplaceholder.typicode.com/posts")
    }


    suspend fun login(): RegisterResponse = apiCall {
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
    }


}



