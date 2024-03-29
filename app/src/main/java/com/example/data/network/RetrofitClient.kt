package com.example.data.network

import com.example.demoproject.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient {

    companion object {
        fun webService(networkConnectionInterceptor: NetworkConnectionInterceptor): ApiInterface {
            val logging = HttpLoggingInterceptor()
            if (BuildConfig.DEBUG) {
                logging.level = HttpLoggingInterceptor.Level.BODY
            } else {
                logging.level = HttpLoggingInterceptor.Level.NONE
            }

            val okClient = OkHttpClient.Builder().apply {
                connectTimeout(15, TimeUnit.SECONDS)
                readTimeout(15, TimeUnit.SECONDS)
                writeTimeout(15, TimeUnit.SECONDS)
                addInterceptor(networkConnectionInterceptor)
                addInterceptor(logging)
            }

            return Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .client(okClient.build())
                .build()
                .create(ApiInterface::class.java)
        }

        private const val BASE_URL = "https://www.rayoinnovations.com/"

        private const val URL = "https://www.jsonkeeper.com/b/THIG/"
    }


}

/*object RetrofitClient {

    private val baseUrl = "https://www.rayoinnovations.com/"

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}*/


