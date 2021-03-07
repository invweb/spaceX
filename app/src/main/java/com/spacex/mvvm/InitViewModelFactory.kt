package com.spacex.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.spacex.application.MyApplication

class InitViewModelFactory(private val myApplication: MyApplication) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InitViewModel(myApplication) as T
    }
}