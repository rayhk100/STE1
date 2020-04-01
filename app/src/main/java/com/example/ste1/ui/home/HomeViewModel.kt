package com.example.ste1.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

     val  product= MutableLiveData<String>().apply {
        value = ""
    }
    val  productin= MutableLiveData<String>().apply {
        value = ""
    }
    val  productnu= MutableLiveData<String>().apply {
        value = ""
    }

    val  reminder= MutableLiveData<String>().apply {
        value = ""
    }
//    private val _text2 = MutableLiveData<String>().apply {
//        value = "The Scanning has failed"
//    }

//    val textfail: LiveData<String> = _text2
}