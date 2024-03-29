package com.example.baseclasses

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.model.CommonResponse
import com.example.utils.ApiException
import com.example.utils.Event

abstract class BaseViewModel : ViewModel() {
    val errorHandler = MutableLiveData<Event<String>>()
    val progressbarObserver = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<Event<String>>()
    val unauthorizedUserHandler = MutableLiveData<Event<String>>()
    private val STATUS_OK = 1

    fun <T : CommonResponse> callAPI(callAPI: T?): T {
        when (callAPI?.success) {
            STATUS_OK -> {
                return callAPI
            }
            else -> {
                val message = StringBuilder()
                if (callAPI != null) {
                    val error = callAPI.message
                    if (error.isEmpty()) {
                        message.append("Something went wrong")
                    } else {
                        message.append(error)
                    }
                } else {
                    message.append("Something went wrong")
                }
                throw ApiException(message.toString())
            }
        }
    }
}