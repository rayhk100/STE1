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
    val newList = MutableLiveData<String>().apply {
        value = ""
    }
//    val itemBinding = MutableLiveData<String>().apply {
//        value = ""
//    }

    val items: ObservableList<ItemViewModel> = ObservableArrayList()
    val itemBinding = ItemBinding.of<ItemViewModel>(BR.item, R.layout.list_items_fragment)
}
