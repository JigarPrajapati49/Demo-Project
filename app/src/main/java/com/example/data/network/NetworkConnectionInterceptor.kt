package com.example.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.utils.NoInternetException
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkConnectionInterceptor @Inject constructor(@ApplicationContext context: Context) : Interceptor {
    private val applicationContext = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isInternetAvailable()) {
            throw NoInternetException("No Internet Connection")
        }
        try {
            return chain.proceed(chain.request())
        } catch (e: Exception) {
            throw NoInternetException("No internet Connection")
        }
    }

    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        connectivityManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                        else -> false
                    }
                }
            } else {
                result =
                    connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo?.isConnected ?: false
            }
        }
        return result
    }

}