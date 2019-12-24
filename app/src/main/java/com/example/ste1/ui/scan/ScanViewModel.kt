package com.example.ste1.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScanViewModel : ViewModel() {
//    val ingre: List<String> = ArrayList<String>().apply {
//        val i = 0;
//        while( i < ArrayList<String>().size){
//            ArrayList<String>().[i] = ""
//            i+1
//        }
//    }


     val name = MutableLiveData<String>().apply {
        value = ""
    }

    val ingre = MutableLiveData<String>().apply {
        value = ""
    }

    // TODO: Implement the ViewModel
}
