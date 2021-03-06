package com.example.ste1.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignupViewModel() : ViewModel() {
    val displayName = MutableLiveData<String>().apply {
        value = ""
    }
    val email = MutableLiveData<String>().apply {
        value = ""
    }
    val password = MutableLiveData<String>().apply {
        value = ""
    }
    val confirmPassword = MutableLiveData<String>().apply {
        value = ""
    }
    val avatar = MutableLiveData<String>().apply {
        value = ""
    }

    val weight = MutableLiveData<String>().apply {
        value = ""
    }

    val height = MutableLiveData<String>().apply {
        value = ""
    }

    val age = MutableLiveData<String>().apply {
        value = ""
    }

    val sex =MutableLiveData<String>().apply {
        value = ""
    }

    fun setSex(value:String) {
        sex.value = value
    }
}