package com.example.ste1.ui.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ste1.MainActivity

import com.example.ste1.R
import com.example.ste1.databinding.ListFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ListFragment : Fragment() {
    val TAG = "ListFragment"
    val db = FirebaseFirestore.getInstance()

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var viewModel: ListViewModel
    private lateinit var binding:ListFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProviders.of(this).get(ListViewModel::class.java)
         binding = ListFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@ListFragment
            vm = viewModel
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        val CollRef = db.collection("User").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).collection("list")

        CollRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            binding.vm!!.items.clear()
            snapshot?.forEach {
//                Log.d(TAG,it.getString("title"))
                //show lists' titles
                val model = ItemViewModel()
                model.title.value = it?.getString("title")
                model.productNum.value = (it?.get("item") as List<String>).size.toString()

                binding.vm!!.items.add(model)

                //check titles
               // Log.d(TAG,it.getString("title").toString())


//                val items= it?.get("item")as List<String>
//
//
//                items.forEach{singleItem ->
//                  // val itemsColRef = db.collection("Product1").document()
//                       MainActivity.AllProduct.get(singleItem)
//                }
//
//
//                model.productName.value = MainActivity.AllProduct.get("")
//
//                    (it?.get("item").     as List<String>).joinToString(
//                    separator = ","
//                ) { it -> "${it}" }






            }
        }

        //button for creating new list
        binding.apply {
            createList.setOnClickListener {
                CollRef.document().set(vm!!.newList)
            }
        }


       // binding.vm.items.add(model)
    }
}
