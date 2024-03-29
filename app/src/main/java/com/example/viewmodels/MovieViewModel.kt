package com.example.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.data.model.MainRepository
import com.example.data.model.Movie

class MovieViewModel constructor(private val mainRepository: MainRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()

    fun getMovieList(): LiveData<PagingData<
            Movie>> {
        return mainRepository.getAllMovies().cachedIn(viewModelScope)
    }

}