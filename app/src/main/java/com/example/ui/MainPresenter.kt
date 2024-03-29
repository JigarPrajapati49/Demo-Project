package com.example.ui

import com.example.data.model.Product

interface MainPresenter {
    fun onWelComeClick()
    fun onItemClick(list: Product)
}