package com.example.ste1.ui.detail


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.ste1.R
import com.example.ste1.databinding.DetailItemFragmentBinding
import com.example.ste1.databinding.DialogReportBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore


class detailItemFragment : Fragment() {
    private lateinit var detailItemViewModel: DetailItemViewModel
    //    private lateinit var camera: CameraView
    private lateinit var binding: DetailItemFragmentBinding
    private lateinit var dialogBinding:DialogReportBinding

    lateinit var dialog:AlertDialog

    val db = FirebaseFirestore.getInstance()

    var problemMap = hashMapOf<Int, String>()

    //    private var lastTime = System.currentTimeMillis()
    companion object {
        fun newInstance() = detailItemFragment()
    }

    private lateinit var ProbviewModel: ProbViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        detailItemViewModel =
            ViewModelProviders.of(this).get(DetailItemViewModel::class.java)
        binding = DetailItemFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@detailItemFragment
            vmProd = detailItemViewModel
        }
        ProbviewModel=ViewModelProviders.of(this).get(ProbViewModel::class.java)
        dialogBinding = DialogReportBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@detailItemFragment
//            vmB = ViewModelProviders.of(this@detailItemFragment).get(ProbviewModel::class.java)
            vmB = ProbviewModel
        }

        //dialog for report
        var mSelectedItems = ArrayList<Int>()

        dialog = AlertDialog.Builder(context!!).setView(dialogBinding.root)
            .setTitle(R.string.dialog_prob)
            .setMultiChoiceItems(R.array.list_prob, null, { dialog, which, isChecked ->
                if (isChecked) {
                    mSelectedItems.add(which);
                } else if (mSelectedItems.contains(which)) {
                    mSelectedItems.remove((which));
                }
            })
            .setPositiveButton(R.string.submit, { dialog, id ->
                val problemRef = db.collection("Problem").document(arguments?.getString("code")!!)
//                Log.d("detail_dialog1", problemRef.get().toString())
                problemRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        mSelectedItems.forEach {

                            var Uping =documentSnapshot.getLong(problemMap[it]!!)
//                            Log.d("detail_dialog2", Uping.toString())
                            problemRef.update(problemMap[it]!!, Uping?.plus(1))

                        }
                    } else {
                        var data = hashMapOf(
                            "Name" to 0,
                            "Nutrition" to 0,
                            "Ingredient" to 0,
//                            "Image" to 0,
                            "Reminder" to 0
                        )

                        problemRef.set(data).addOnSuccessListener {
                            mSelectedItems.forEach {
                                problemRef.update(problemMap[it]!!,1)
//                                Log.d("detail_dialog3", problemMap[it].toString())
                            }
                        }
                    }
                }

                    val comment = dialogBinding.vmB?.detail?.value.toString()
                if(comment?.isNotEmpty()){
                    Log.d("detail_dialog4 ", comment)
                    val time = FieldValue.serverTimestamp()
                    val user =FirebaseAuth.getInstance().currentUser?.uid!!
                    val data= hashMapOf<String,Any>(
                        "User" to user,
                        "Time" to time,
                        "Detail" to comment
                    )
                    problemRef.collection("comment").add(data)
                }else{
                    Log.d("detail_dialog_no ", " no comment")
                    val time = FieldValue.serverTimestamp()
                    val user =FirebaseAuth.getInstance().currentUser?.uid!!
                    val data= hashMapOf<String,Any>(
                        "User" to user,
                        "Time" to time,
                        "Detail" to "no comment"
                    )
                    problemRef.collection("comment").add(data)
                }

            })
            .setNegativeButton(R.string.cancel, { dialog, id ->
                dialog.cancel()
            }).create()


        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//            viewModel = ViewModelProviders.of(this).get(DetailItemViewModel::class.java)
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
                                when (item.id){
                                    "S fat"-> "Saturated fat 飽和脂肪: ${item.getLong("value")} ${item.getString("unit")}"
                                    "T fat"-> "Trans fat 反式脂肪: ${item.getLong("value")} ${item.getString("unit")}"
                                    "carbo"-> "Carbohydrate 碳水化合物: ${item.getLong("value")} ${item.getString("unit")}"
                                    "energy"->"${item.id} 熱量: ${item.getLong("value")} ${item.getString("unit")}"
                                    "fat"->"Total fat 脂肪總量: ${item.getLong("value")} ${item.getString("unit")}"
                                    "protein"->"${item.id} 蛋白質: ${item.getLong("value")} ${item.getString("unit")}"
                                    "sodium"->"${item.id} 鈉: ${item.getLong("value")} ${item.getString("unit")}"
                                    "sugar"->"${item.id} 糖: ${item.getLong("value")} ${item.getString("unit")}"

                                    else->"${item.id}: ${item.getLong("value")} ${item.getString("unit")}"
                                }
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
                    ) { it -> "${it}" }.contains("Egg") ||
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

        }



        binding.button2.setOnClickListener {
//            onCreateDialog(savedInstanceState)?.show()
            dialog.show()
        }

        resources.getStringArray(R.array.list_prob).forEachIndexed { index, s ->
            problemMap[index] = s
        }
    }

//    fun onCreateDialog(savedInstanceState: Bundle?): Dialog? {
//
////        val vmB = ProbViewModel()
//        var mSelectedItems = ArrayList<Int>()
//        val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
//        val db = FirebaseFirestore.getInstance()
//        val docRef = db.collection("Problem")
//
//
//
////        val inflater = activity!!.layoutInflater
////        inflater.inflate(R.layout.dialog_report, null)
//
//
//
//        builder.setView()
//            .setTitle(R.string.dialog_prob)
//            .setMultiChoiceItems(R.array.list_prob, null, { dialog, which, isChecked ->
//                if (isChecked) {
//                    mSelectedItems.add(which);
//                } else if (mSelectedItems.contains(which)) {
//                    mSelectedItems.remove((which));
//                }
//
////                @Override
////
////                fun onClick(dialog: DialogInterface, which: String,
////                            isChecked:Boolean ) {
////                    if (isChecked) {
////                        // If the user checked the item, add it to the selected items
////                        mSelectedItems.add(which);
////                    } else if (mSelectedItems.contains(which)) {
////                        // Else, if the item is already in the array, remove it
////                        mSelectedItems.remove((which));
////                    }
////                }
//
//            })
//            .setPositiveButton(R.string.submit, { dialog, id ->
//                val problemRef = docRef.document(arguments?.getString("code")!!)
////                Log.d("detail_dialog1", problemRef.get().toString())
//                problemRef.get().addOnSuccessListener { documentSnapshot ->
//                    if (documentSnapshot.exists()) {
//                        mSelectedItems.forEach {
//
//                            var Uping =documentSnapshot.getLong(problemMap[it]!!)
//
//
////                            Log.d("detail_dialog2", Uping.toString())
//                            problemRef.update(problemMap[it]!!, Uping?.plus(1))
//
//
//                        }
//                    } else {
//                        var data = hashMapOf(
//                            "Name" to 0,
//                            "Nutrition" to 0,
//                            "Ingredient" to 0,
//                            "Image" to 0,
//                            "Reminder" to 0
//                        )
//
//                        problemRef.set(data).addOnSuccessListener {
//                            mSelectedItems.forEach {
//                                problemRef.update(problemMap[it]!!,1)
////                                Log.d("detail_dialog3", problemMap[it].toString())
//                            }
//                        }
//                    }
//                }
//
////                if(vmB.detail.value!=null){
////                    var Comment = vmB.detail.value
////                    Log.d("detail_dialog4 ", Comment)
////                    problemRef.collection("comment").add("Comment" to Comment)
////                }
//
////                if (docRef.document(arguments?.getString("code")!!).path.isEmpty()) {
////                    //var dataP =
////
////
////                       // docRef.document(arguments?.getString("code")!!).set(dataP)
////                }
////                val CurrNo = docRef.document(arguments?.getString("code")!!)
////                mSelectedItems.forEach { prob ->
////                    Log.d("detail_dialog: ", prob.toString())
////
////                    when (prob) {
////                        0 -> CurrNo.addSnapshotListener { snapshot, firebaseFirestoreException ->
////                            var value = snapshot?.get("Name")
////                            CurrNo.update("Name", (Int) value +1)
////                        }
////
////
////                    }
////                }
//            })
//            .setNegativeButton(R.string.cancel, { dialog, id ->
//                dialog.cancel()
//            })
//
//
//
//
//
//
//
//
//
//        return builder.create();
//    }


//        return binding.root
}

