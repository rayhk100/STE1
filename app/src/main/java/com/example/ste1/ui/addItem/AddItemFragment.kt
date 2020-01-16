package com.example.ste1.ui.addItem

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import com.example.ste1.R

import com.example.ste1.databinding.AddItemFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class AddItemFragment : Fragment() {
    private lateinit var binding: AddItemFragmentBinding
    //val db = FirebaseFirestore.getInstance()
    val TAG ="AddItemFragment"

    companion object {
        fun newInstance() = AddItemFragment()
    }

    private lateinit var viewModel: AddItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AddItemFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@AddItemFragment
            vm = ViewModelProviders.of(this@AddItemFragment).get(AddItemViewModel::class.java)
        }
//        binding

        val adapter = ArrayAdapter.createFromResource(context!!, R.array.list_ingr, android.R.layout.simple_list_item_1)
        binding.itemIngre.setAdapter(adapter)
        binding.itemIngre.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProviders.of(this).get(AddItemViewModel::class.java)

//        binding.item_ingre.adapter = ArrayAdapter<String>(context, arrayOf(""))
        binding.apply {
            addItemSubmit.setOnClickListener {
                view ->
                if (vm?.code?.value.isNullOrEmpty() || vm?.name?.value.isNullOrEmpty()||
                    vm?.ingre?.value.isNullOrEmpty() || vm?.nutri_carbo?.value.isNullOrEmpty()||
                    vm?.nutri_energy?.value.isNullOrEmpty() || vm?.nutri_fat?.value.isNullOrEmpty()||
                    vm?.nutri_protein?.value.isNullOrEmpty() || vm?.nutri_sfat?.value.isNullOrEmpty()||
                    vm?.nutri_tfat?.value.isNullOrEmpty() || vm?.nutri_sugar?.value.isNullOrEmpty()||
                    vm?.nutri_sodium?.value.isNullOrEmpty() ) {
                    Snackbar.make(
                        view,
                        "Please input all data.",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Action", null).show()
                } else {
                    Log.d("addItemFragment", "adding")
                val db = FirebaseFirestore.getInstance()
                    val pathdoc =vm?.code?.value.toString()
                    val ingrelist = vm?.ingre?.value!!.split(", ").map { it.trim() }.filter { it != "" }
                    val data = hashMapOf("name" to vm?.name?.value,

//                        "ingre" to vm?.ingre?.value
                        "ingre" to ingrelist

                        )
                    val dataer =hashMapOf(
                        "value" to  vm?.nutri_energy?.value!!.toDouble(),
                        "unit" to "kcal"
                    )
                    val datapr =hashMapOf(
                        "value" to  vm?.nutri_protein?.value!!.toDouble(),
                        "unit" to "g"
                    )
                    val datafa =hashMapOf(
                        "value" to  vm?.nutri_fat?.value!!.toDouble(),
                        "unit" to "g"
                    )
                    val datasf =hashMapOf(
                        "value" to  vm?.nutri_sfat?.value!!.toDouble(),
                        "unit" to "g"
                    )
                    val datatf =hashMapOf(
                        "value" to  vm?.nutri_tfat?.value!!.toDouble(),
                        "unit" to "g"
                    )
                    val dataca =hashMapOf(
                        "value" to  vm?.nutri_carbo?.value!!.toDouble(),
                        "unit" to "g"
                    )
                    val datasu =hashMapOf(
                        "value" to  vm?.nutri_sugar?.value!!.toDouble(),
                        "unit" to "g"
                    )
                    val dataso =hashMapOf(
                        "value" to  vm?.nutri_sodium?.value!!.toDouble(),
                        "unit" to "g"
                    )
                    if(db.collection("Product1").document(pathdoc)!=null){
                        Log.d("addItemFragment", "adding with merge")
                     db.collection("Product1").document(pathdoc).set(data, SetOptions.merge())
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("energy").set(dataer, SetOptions.merge())
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("protein").set(datapr, SetOptions.merge())
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("fat").set(datafa, SetOptions.merge())
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("S fat").set(datasf, SetOptions.merge())
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("T fat").set(datatf, SetOptions.merge())
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("carbo").set(dataca, SetOptions.merge())
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("sugar").set(datasu, SetOptions.merge())
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("sodium").set(dataso, SetOptions.merge())

                    }else{
                        Log.d("addItemFragment", "add")
                     db.collection("Product1").document(pathdoc).set(data)
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("energy").set(dataer)
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("protein").set(datapr)
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("fat").set(datafa)
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("S fat").set(datasf)
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("T fat").set(datatf)
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("carbo").set(dataca)
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("sugar").set(datasu)
                        db.collection("Product1").document(pathdoc)
                            .collection("nutri").document("sodium").set(dataso)

                        Snackbar.make(
                            view,
                            "data created",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("Action", null).show()
                    }




//                docRef.addSnapshotListener { snapshot, e ->
//                    if (e != null) {
//                        Log.w(TAG, "Listen failed.", e)
//                        return@addSnapshotListener
//                    }
//
//                }
                // TODO: Use the ViewModel

            }
            }

        }
}
}
