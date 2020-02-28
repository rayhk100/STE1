package com.example.ste1

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import kotlin.properties.Delegates

data class Product(
 var _productName : String,
// var _energy : Double,
// var _protein : Double,
// var _fat : Double,
// var _tfat : Double,
// var _sfat : Double,
// var _carbo : Double,
// var _sugar : Double,
// var _sodium : Double,
 var nutri : List<Double>




) : BaseObservable()
 {
//
//    var energy :Long by Delegates.observable(0)
//    {prop, old , new -> notifyPropertyChanged(BR.)}
}