package com.example.ste1.ui.header

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HeaderViewModel : ViewModel() {
    val avatar = MutableLiveData<String>().apply {
        value = ""
    }

    val name = MutableLiveData<String>().apply {
        value = ""
    }

    val email = MutableLiveData<String>().apply {
        value = ""
    }
}