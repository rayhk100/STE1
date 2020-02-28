package com.example.ste1.ui.detail

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailItemViewModel : ViewModel(){
    // TODO: Implement the ViewModel
//    @Bindable
    val  product= MutableLiveData<String>().apply {
        value = ""
    }
//    @Bindable
    val  productin= MutableLiveData<String>().apply {
        value = ""
    }
//    @Bindable
    val  productnu= MutableLiveData<String>().apply {
        value = ""
    }
//    @Bindable
    val  reminder= MutableLiveData<String>().apply {
        value = ""
    }



//    @Bindable
    val energy= MutableLiveData<Double>().apply{
        value= 0.0
    }
//    @Bindable
    val protein= MutableLiveData<Double>().apply{
        value= 0.0
    }
//    @Bindable
    val fat= MutableLiveData<Double>().apply{
        value= 0.0
    }
//    @Bindable
    val sfat= MutableLiveData<Double>().apply{
        value= 0.0
    }
//    @Bindable
    val tfat= MutableLiveData<Double>().apply{
        value= 0.0
    }
//    @Bindable
    val carbo= MutableLiveData<Double>().apply{
        value= 0.0
    }
//    @Bindable
    val sugar= MutableLiveData<Double>().apply{
        value= 0.0
    }
//    @Bindable
    val sodium= MutableLiveData<Double>().apply{
        value= 0.0
    }
}
