package com.example.ktor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.baseclasses.BaseViewModel
import com.example.data.model.ProductResponce
import com.example.ktor.data.Resource
import com.example.ktor.data.model.Cat
import com.example.ktor.data.model.Movie
import com.example.ktor.data.model.Post
import com.example.ktor.data.model.UserEntity
import com.example.ktor.data.network.ApiInterfaceKtor
import com.example.utils.Coroutines
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: ApiInterfaceKtor,
    private val repo: CollectionsServiceImpl,
    /*private val repositorys: ApiRepositoryKtor,*/
) : BaseViewModel() {

    val getData = MutableLiveData<List<Movie>>()
    private val post = MutableStateFlow<Resource<List<Post>>?>(null)


    val productData = MutableLiveData<ProductResponce>()
    val userEntity = MutableLiveData<UserEntity>()


    init {
        getPost()
        login()
        getCatData()
        posts()
        withException()
        withCommonFunction()
        /*repositroyMVVM()*/
        withCommonResponce()
    }


    fun getDummyData() {
        Coroutines.ioThenMain({
            repository.getPopularMovies()
        }, {
            Log.e("TAG", "getPopularMovies: -__${it?.size}")
            getData.postValue(it)
        })
    }

    fun getPost() {
        Coroutines.ioThenMain({
            repository.getProduct()
        }, {
            Log.e("TAG", "getProduct: -__${it?.status}")
//            getData.postValue(it)
            productData.postValue(it)
        })
    }

    fun getCatData() {
        Coroutines.ioThenMain({
            repository.getCatData()
        }, {
            Log.e("TAG", "getCatData: -__${it}")
//            getData.postValue(it)

        })
    }

    private fun login() {
        Coroutines.ioThenMain({
            repository.login()
        }, {
            Log.e("TAG", "LOGIN---------${Gson().toJson(it)}")
        })
    }

    private fun posts() {
        Coroutines.ioThenMain({
            repository.getPost()
        }, {
            Log.e("TAG", "POSTS---==-=-=========-${Gson().toJson(it)}")
        })
    }

    private fun withException() {
        Coroutines.ioThenMain({
            repo.getPost()
        }, {
            Log.e("TAG", "POSTS--WITH--EXCEPTION-${Gson().toJson(it)}")
        })
    }

    private fun withCommonFunction() {
        Coroutines.io {
            ApiCall.executeApiCall<List<Cat>>(
                "https://api.thecatapi.com/v1/images/search",
                onSuccess = { data ->
                    // Handle successful response
                    Log.d("TAG", "Success: $data")
                },
                onError = { errorMessage ->
                    // Handle error
                    Log.e("TAG", "Error: $errorMessage")
                },
            )
        }
    }

    private fun withCommonResponce() {
        Coroutines.ioThenMain({
            repository.getPostWithCommonResponce()
        }, {
            post.value = Resource.Loading
            post.value = it

        })
    }

     /*private fun repositroyMVVM() {
         Coroutines.io {
             repository.fetchData()
         }
     }*/


}