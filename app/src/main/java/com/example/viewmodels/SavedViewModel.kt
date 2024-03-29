package com.example.viewmodels

import com.example.baseclasses.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor() : BaseViewModel() {
    var countValue = 0
}