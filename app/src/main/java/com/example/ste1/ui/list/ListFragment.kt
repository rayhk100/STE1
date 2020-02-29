package com.example.ste1.ui.list

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ste1.MainActivity
import com.example.ste1.MainActivity.Companion.AllProduct
import com.example.ste1.MainActivity.Companion.AllProduct2
import com.example.ste1.Product

import com.example.ste1.R
import com.example.ste1.databinding.ListFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.detail_item_fragment.*
import java.time.LocalDateTime
import java.util.*

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
        var Totalca=0.0
        var Totalen=0.0
        var Totalpr=0.0
        var Totalfa=0.0
        var Totalsf=0.0
        var Totaltf=0.0
        var Totalso=0.0
        var Totalsu=0.0
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        val CollRef = db.collection("User").document(FirebaseAuth.getInstance().currentUser?.uid.toString()).collection("list")
//        val itemRef =db.collection("Product1")
        //Log.d(TAG+"test",CollRef.orderBy("updateAt").startAt(LocalDateTime.now().dayOfYear).toString())
        val time=LocalDateTime.now().toLocalDate()


        Log.d(TAG+"test",time.toString())
//        CollRef.orderBy("updateAt").addSnapshotListener { snapshot, e ->
        CollRef.orderBy("updateAt").addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
//            var TotalList= listOf<Double>(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0)

            binding.vm!!.items.clear()
            snapshot?.forEach {
//                Log.d(TAG,it.getString("title"))
                //show lists' titles
                val model = ItemViewModel()
              //  model.title.value = it?.getString("title")

//                model.productNum.value = (it?.get("item") as List<String>).size.toString()

//                model.title.value = itemRef.document(it?.getString("item")).get("name".).toString()
//                Log.d("List_Item_Title", AllProduct["Product1/"+it?.getString("item")]!!.product.value)


                model.title.value = AllProduct["Product1/"+it?.getString("item")]!!.product.value

                Log.d("List_Item_Title", it?.getTimestamp("updateAt").toString())
                if(it?.getTimestamp("updateAt")!=null){model.AddDate.value = it?.getTimestamp("updateAt")?.toDate().toString()}
//

//                Log.d(TAG+"Test",Totalca.toString())
                if(it?.getString("timeAt").toString().contains(time.toString()))
                {  
                    Totalsf=Totalsf.plus(AllProduct["Product1/"+it?.getString("item")]!!.sfat.value!!)
                    Totaltf=Totaltf.plus(AllProduct["Product1/"+it?.getString("item")]!!.tfat.value!!)
                    Totalca=Totalca.plus(AllProduct["Product1/"+it?.getString("item")]!!.carbo.value!!)
                    Totalen=Totalen.plus(AllProduct["Product1/"+it?.getString("item")]!!.energy.value!!)
                    Totalfa=Totalfa.plus(AllProduct["Product1/"+it?.getString("item")]!!.fat.value!!)
                    Totalpr=Totalpr.plus(AllProduct["Product1/"+it?.getString("item")]!!.protein.value!!)
                    Totalso=Totalso.plus(AllProduct["Product1/"+it?.getString("item")]!!.sodium.value!!)
                    Totalsu=Totalsu.plus(AllProduct["Product1/"+it?.getString("item")]!!.sugar.value!!)
                    
                    binding.vm!!.items.add(model)}

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
//                    Log.d(TAG+"Test2",Totalca.toString())
            viewModel.TotalCa.value= "carbo: "+Totalca.toString()+" g"
            viewModel.TotalEn.value="energy: "+Totalen.toString()+" kcal"
            viewModel.TotalFa.value= "fat: "+Totalfa.toString()+" g"
            viewModel.TotalPr.value="protein: "+Totalpr.toString()+" g"
            viewModel.TotalSo.value="sodium: "+Totalso.toString()+" mg"
            viewModel.TotalSu.value="sugar: "+Totalsu.toString()+" g"
            viewModel.TotalSF.value="S fat: "+Totalsf.toString()+" g"
            viewModel.TotalTF.value="T fat: "+Totaltf.toString()+" g"

            viewModel.Total.value="Total:    " + viewModel.TotalSF.value  + "| "+ viewModel.TotalTF.value  + "| "+ viewModel.TotalCa.value  + "| "+ viewModel.TotalEn.value  + "| "+ viewModel.TotalFa.value  + "| "+ viewModel.TotalPr.value  + "| "+ viewModel.TotalSo.value  + "| "+ viewModel.TotalSu.value


//            viewModel.Total.value=
        }
        db.collection("standard").document("001").collection("nutri").addSnapshotListener{querySnapshot, firebaseFirestoreException ->
            viewModel.standard.value="Standard: "+
                querySnapshot?.documents?.map {
                        item ->
                "${item.id}: ${item.getLong("value")} ${item.getString("unit")}"

               // Log.d("ListFragment",item.id+" "+item.getLong("value"))

            }?.joinToString(", ")}



        //button for creating new list
//        binding.apply {
//            createList.setOnClickListener {
//                CollRef.document().set(vm!!.newList)
//            }
//        }


       // binding.vm.items.add(model)
    }
}
