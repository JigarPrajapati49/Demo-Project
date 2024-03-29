package com.example.viewmodels

import android.app.Activity
import android.widget.Toast
import com.example.baseclasses.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomSheetViewModel @Inject constructor(): BaseViewModel() {

    fun showSomething(context: Activity){
        Toast.makeText(context, "From View Model", Toast.LENGTH_SHORT).show()
    }
}