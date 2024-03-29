package com.example.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.network.repository.ApiRepository
import com.example.db.ProductDatabase

class MainViewModelFactory(private val repository: ApiRepository, private val data : ProductDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository,data) as T
    }
}