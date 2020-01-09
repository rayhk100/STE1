package com.example.ste1.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel() : ViewModel() {
    val email = MutableLiveData<String>().apply {
        value = ""
    }

    val password = MutableLiveData<String>().apply {
        value = ""
    }
}
