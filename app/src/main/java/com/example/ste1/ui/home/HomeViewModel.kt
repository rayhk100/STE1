package com.example.ste1.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "The Scanning has started"
    }
    private val _text2 = MutableLiveData<String>().apply {
        value = "The Scanning has failed"
    }
    val text: LiveData<String> = _text
    val textfail: LiveData<String> = _text2
}