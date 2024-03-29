package com.example.ktor.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.baseclasses.BaseViewModel
import com.example.ktor.RegisterResponse
import com.example.ktor.exception.ApiCallException
import com.example.ktor.data.network.repository.ApiRepositoryKtor
import com.example.utils.Coroutines
import com.example.utils.Event
import com.example.utils.NoInternetException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(private val repository: ApiRepositoryKtor) : BaseViewModel() {

    private val loginResponce = MutableLiveData<RegisterResponse>()

    init {
        repositroyMVVM()
        loginwithNormalApiCall()
        loginWithProperApiCall()
    }

    val data = liveData(Dispatchers.IO) {
        try {
            val result = repository.fetchData()
            Log.e("TAG", "=====: -${result.size}")
            val t = emit(result)
        } catch (e: ApiCallException.ApiCallException) {
            // Handle API-related exceptions (client or server-side errors)
            // Show appropriate error message to the user
            errorHandler.postValue(Event(e.message))
        } catch (e: Exception) {
            // Handle other general exceptions
            // Show appropriate error message to the user
            errorHandler.postValue(Event(e.message))
        }


    }

    val withRepo = liveData(Dispatchers.IO) {
        try {
            emit(repository.fetchData())
        } catch (e: ApiCallException.ApiCallException) {
            // Handle API-related exceptions (client or server-side errors)
            // Show appropriate error message to the user
        } catch (e: Exception) {
            // Handle other general exceptions
            // Show appropriate error message to the user
        }
    }

    private fun repositroyMVVM() {
        Coroutines.io {
            try {
                val result = (repository.fetchData())
                Log.e("TAG", "repositroyMVVM: -==============${result.size}")
            } catch (e: ApiCallException.ApiCallException) {
                // Handle API-related exceptions (client or server-side errors)
                // Show appropriate error message to the user
                Log.e("TAG", "ApiCallException: --==========${e.message}")
                errorHandler.postValue(Event(e.message))
            } catch (e: Exception) {
                // Handle other general exceptions
                // Show appropriate error message to the user
                Log.e("TAG", "Exception: --==========${e.message}")
                errorHandler.postValue(Event(e.message))
            }
        }
    }

    private fun loginwithNormalApiCall() {
        Coroutines.io {
            try {
                val result = (repository.login())
                callAPI(result)
                Log.e("TAG", "Login With Repository: -${result}")
            } catch (e: ApiCallException.ApiCallException) {
                // Handle API-related exceptions (client or server-side errors)
                // Show appropriate error message to the user
                Log.e("TAG", "ApiCallException: --==========${e.message}")
                errorHandler.postValue(Event(e.message))
            } catch (e: Exception) {
                // Handle other general exceptions
                // Show appropriate error message to the user
                Log.e("TAG", "Exception: --==========${e.message}")
                errorHandler.postValue(Event(e.message))
            }
        }
    }

    private fun loginWithProperApiCall() {
        Coroutines.ioThenMain({
            try {
                val login = repository.login()
                callAPI(login)
            } catch (e: ApiCallException.ApiExceptions) {
                errorMessage.postValue(Event(e.message))
            } catch (e: NoInternetException) {
                errorMessage.postValue(Event(e.message))
            } catch (e: ApiCallException.UnauthorizedUserException) {
                unauthorizedUserHandler.postValue(Event(e.message))
            }
        }, {
            if (it != null && it is RegisterResponse) {
                loginResponce.postValue(it)
            }
        })
    }

}
