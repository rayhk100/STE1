package com.example.ste1.ui.list

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ste1.BR
import com.example.ste1.R
import kotlinx.android.synthetic.main.list_fragment.view.*
import me.tatarka.bindingcollectionadapter2.ItemBinding

class ListViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val standard = MutableLiveData<String>().apply {
        value = ""
    }
    val standardEn = MutableLiveData<String>().apply {
        value = ""
    }
    val standardPr = MutableLiveData<String>().apply {
        value = ""
    }
    val standardFa = MutableLiveData<String>().apply {
        value = ""
    }
    val standardSF = MutableLiveData<String>().apply {
        value = ""
    }
    val standardTF = MutableLiveData<String>().apply {
        value = ""
    }
    val standardCa = MutableLiveData<String>().apply {
        value = ""
    }
    val standardSu = MutableLiveData<String>().apply {
        value = ""
    }
    val standardSo = MutableLiveData<String>().apply {
        value = ""
    }

    val Total = MutableLiveData<String>().apply {
        value = ""
    }
    val TotalEn= MutableLiveData<String>().apply {
        value = ""
    }
    val TotalPr= MutableLiveData<String>().apply {
        value = ""
    }
    val TotalFa= MutableLiveData<String>().apply {
        value = ""
    }
    val TotalSF=MutableLiveData<String>().apply {
        value = ""
    }
    val TotalTF= MutableLiveData<String>().apply {
        value = ""
    }
    val TotalCa= MutableLiveData<String>().apply {
        value = ""
    }
    val TotalSu= MutableLiveData<String>().apply {
        value = ""
    }
    val TotalSo= MutableLiveData<String>().apply {
        value = ""
    }



//    val itemBinding = MutableLiveData<String>().apply {
//        value = ""
//    }

    val items: ObservableList<ItemViewModel> = ObservableArrayList()
    val itemBinding = ItemBinding.of<ItemViewModel>(BR.itemO, R.layout.list_items_fragment)
}
