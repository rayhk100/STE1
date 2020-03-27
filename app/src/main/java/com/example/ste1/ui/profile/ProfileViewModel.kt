package com.example.ste1.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    val avatar = MutableLiveData<String>().apply {
        value = ""
    }

    val displayName = MutableLiveData<String>().apply {
        value = ""
    }

    val email = MutableLiveData<String>().apply {
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

    val sex = MutableLiveData<String>().apply {
        value = ""
    }

    fun setSex(value:String) {
        sex.value = value
    }
}