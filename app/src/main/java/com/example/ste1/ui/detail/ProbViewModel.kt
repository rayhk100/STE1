package com.example.ste1.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProbViewModel : ViewModel(){

    val code= MutableLiveData<String>().apply {
        value = ""


    }
    val detail=MutableLiveData<String>().apply {
        value = ""

    }
}