package com.example.ste1.ui.addItem

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddItemViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val name = MutableLiveData<String>().apply {
        value = ""
    }
    val ingre =MutableLiveData<String>().apply {
        value = ""
    }

    val nutri_energy = MutableLiveData<String>().apply{
        value = ""
    }
    val nutri_protein = MutableLiveData<String>().apply{
        value = ""
    }
    val nutri_fat = MutableLiveData<String>().apply{
        value = ""
    }
    val nutri_sfat = MutableLiveData<String>().apply{
        value = ""
    }
    val nutri_tfat = MutableLiveData<String>().apply{
        value = ""
    }
    val nutri_carbo = MutableLiveData<String>().apply{
        value = ""
    }
    val nutri_sugar = MutableLiveData<String>().apply {
        value = ""
    }
    val nutri_sodium = MutableLiveData<String>().apply{
        value = ""
    }
    val code = MutableLiveData<String>().apply {
        value = ""
    }
    val quan = MutableLiveData<String>().apply {
        value = ""
    }
}
