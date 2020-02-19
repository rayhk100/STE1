package com.example.ste1.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ItemViewModel() : ViewModel() {
    val  AddDate= MutableLiveData<String>().apply {
        value = ""
    }
    val  title= MutableLiveData<String>().apply {
        value = ""
    }
//    val  productNutri= MutableLiveData<String>().apply {
//        value = ""
//    }
    val code=MutableLiveData<String>().apply {
    value = ""
}

}