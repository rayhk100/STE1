package com.example.ste1.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScanViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is scan Fragment"
    }
    val text: LiveData<String> = _text
    // TODO: Implement the ViewModel
}
