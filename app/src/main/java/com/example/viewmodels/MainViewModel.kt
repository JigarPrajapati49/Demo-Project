package com.example.viewmodels

import androidx.lifecycle.MutableLiveData
import com.example.baseclasses.BaseViewModel
import com.example.data.model.Product
import com.example.data.network.repository.ApiRepository
import com.example.db.ProductDatabase
import com.example.utils.ApiException
import com.example.utils.Coroutines
import com.example.utils.Event
import com.example.utils.NoInternetException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ApiRepository,
    private val database: ProductDatabase
) : BaseViewModel() {

    val getData = MutableLiveData<List<Product>>()
    fun getDataFromDatabase() {
        Coroutines.main {
            try {
                progressbarObserver.value = true
                val data = repository.getProductFromDatabase()
                data.let {
                    if (it.isEmpty()) {
                        getFromApiAndAddInDatabase()
                    } else {
                        progressbarObserver.value = false
                        getData.postValue(data)
                    }
                }
            } catch (e: ApiException) {
                errorHandler.postValue(Event(e.message))
            } catch (e: NoInternetException) {
                errorHandler.postValue(Event(e.message))
            }
        }
    }

    private fun getFromApiAndAddInDatabase() {
        Coroutines.main {
            try {
                progressbarObserver.value = true
                val settingsData = repository.getDataFromApi()
                settingsData.let {
                    progressbarObserver.value = false
                    database.productDao().addProduct(settingsData.data.productList)
                    getDataFromDatabase()
                }
            } catch (e: ApiException) {
                errorHandler.postValue(Event(e.message))
            } catch (e: NoInternetException) {
                errorHandler.postValue(Event(e.message))
            }
        }
    }

    fun updateDataFromDatabase(product: Product) {
        Coroutines.main {
            try {
                progressbarObserver.value = true
                val data = repository.updateProductFromDatabase(product)
                data.let {
                    progressbarObserver.value = false
                    repository.updateData(product)
                }
            } catch (e: Exception) {
                errorHandler.postValue(Event(e.message))
            } catch (e: NoInternetException) {
                errorHandler.postValue(Event(e.message))
            }
        }
    }


}