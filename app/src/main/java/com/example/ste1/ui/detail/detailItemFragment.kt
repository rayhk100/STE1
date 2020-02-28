package com.example.ste1.ui.detail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import com.example.ste1.R
import com.example.ste1.databinding.DetailItemFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime

class detailItemFragment : Fragment() {
    private lateinit var detailItemViewModel: DetailItemViewModel
    //    private lateinit var camera: CameraView
    private lateinit var binding: DetailItemFragmentBinding

    //    private var lastTime = System.currentTimeMillis()
    companion object {
        fun newInstance() = detailItemFragment()
    }

    private lateinit var viewModel: DetailItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        detailItemViewModel =
            ViewModelProviders.of(this).get(detailItemViewModel::class.java)
        binding = DetailItemFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@detailItemFragment
            vmProd = detailItemViewModel
        }
        return binding.root
    }


        override fun onActivityCreated(savedInstanceState: Bundle?) {
            super.onActivityCreated(savedInstanceState)
            viewModel = ViewModelProviders.of(this).get(DetailItemViewModel::class.java)
            // TODO: Use the ViewModel

//            val contents =
            val TAG = "DeatilItem"
            val db = FirebaseFirestore.getInstance()
            val docRef = db.collection("Product1")
                .document(arguments?.getString("code")!!)

            docRef.addSnapshotListener { snapshot, e ->

                if (e != null) {
                    Log.w(TAG, "Listen failed. ${e}")
                    detailItemViewModel.product.value = "The product does not have  \n " +
                            "description."
                    // binding.button3.visibility = View.VISIBLE
//                                        binding.apply {
//                                            button3.setOnClickListener{
//                                                    view -> findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavAddnewitem())
//                                            }
//                                        }

                    // binding.buttonCountToList.isClickable = false
                    return@addSnapshotListener
                }
                if (snapshot == null || !snapshot?.exists()!!) {
                    Log.w(TAG, "DB no data")
                    detailItemViewModel.product.value = "The product is not in the database, \n " +
                            "we welcome you to add it to database. "
                    detailItemViewModel.productnu.value = ""
                    detailItemViewModel.productin.value = ""
                    detailItemViewModel.reminder.value = ""

                }


                snapshot?.exists()?.let {
                    if (!it) return@addSnapshotListener
                    // binding.button3.visibility = View.GONE
                    detailItemViewModel.reminder.value = ""
                    Log.d(TAG, "find record")
                    detailItemViewModel.product.value = snapshot?.getString("name")
//            viewModel?.text?.value = snapshot?.getString("ingre")
                    detailItemViewModel.productin.value =
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ", "
                        ) { it -> "${it}" }

                    snapshot?.reference?.collection("nutri")
                        ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                            detailItemViewModel.productnu.value =
                                querySnapshot?.documents?.map { item ->
                                    "${item.id}: ${item.getLong("value")} ${item.getString("unit")}"
                                    // Log.d("HomeFragment",item.id)

                                }?.joinToString(", \n")


                            //                                                        if(item.id=="sugar"&&item.getLong("value")!!.compareTo(10.0)>1){
//                                                            homeViewModel.reminder.value="This product has high amount of sugar. \n"
//                                                        }
                        }



                    if ((snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("milk") ||
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("Milk") ||
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("egg") ||
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("Egg")  ||
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("wheat") ||
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("Wheat") ||
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("soy") ||
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("Soy") ||
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("barley") ||
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("Barley")


                    ) {
                        detailItemViewModel.reminder.value = "Ingredients may cause allergy:\n"


                    }
                    if ((snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("milk") ||
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("Milk")
                    ) {
                        detailItemViewModel.reminder.value =
                            detailItemViewModel.reminder.value.plus("milk")
                    }

                    if ((snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("egg") ||
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("Egg")
                    ) {
                        detailItemViewModel.reminder.value =
                            detailItemViewModel.reminder.value.plus("egg")
                    }
                    if ((snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("wheat") ||
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("Wheat")
                    ) {
                        detailItemViewModel.reminder.value =
                            detailItemViewModel.reminder.value.plus("wheat")
                    }
                    if ((snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("soy") ||
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("Soy")
                    ) {
                        detailItemViewModel.reminder.value =
                            detailItemViewModel.reminder.value.plus("soy")
                    }
                    if ((snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("nut") ||
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("Nut")
                    ) {
                        detailItemViewModel.reminder.value =
                            detailItemViewModel.reminder.value.plus("nut")
                    }
                    if ((snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("barley") ||
                        (snapshot?.get("ingre") as List<String>).joinToString(
                            separator = ","
                        ) { it -> "${it}" }.contains("Barley")
                    ) {
                        detailItemViewModel.reminder.value =
                            detailItemViewModel.reminder.value.plus("barley")
                    }


//                                }
                }

            } }



//        return binding.root
    }

