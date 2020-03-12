package com.example.ste1

import android.util.Log
import android.view.View
import androidx.navigation.Navigation.findNavController

class ClickHandler {
        fun clickTo(view: View,code: String) {
                Log.d("class_click","clickHandler")
//                (view.rootView.context as MainActivity).clickHandler(view,code)
        }
}