package com.example.ste1.ui.chart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChartViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val standard = MutableLiveData<String>().apply {
        value = ""
    }

    val goal_Ref_max= MutableLiveData<Int>().apply {
        value = 0
    }
    val goal_Ref_min= MutableLiveData<Int>().apply {
        value = 0
    }


}
