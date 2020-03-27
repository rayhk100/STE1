package com.example.ste1.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.ste1.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.jakewharton.threetenabp.AndroidThreeTen
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.frame.FrameProcessor
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
//import jdk.nashorn.internal.objects.NativeDate.getTime
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HomeFragment : Fragment() {
//    private lateinit var database: DatabaseReference
    private lateinit var homeViewModel: HomeViewModel
//    private lateinit var camera: CameraView
    private lateinit var binding: FragmentHomeBinding
            private var lastTime = System.currentTimeMillis()


    val TAG = "HomeFragment"
    val db = FirebaseFirestore.getInstance()
    val options = FirebaseVisionBarcodeDetectorOptions.Builder()
        .setBarcodeFormats(
//            FirebaseVisionBarcode.FORMAT_QR_CODE,
//            FirebaseVisionBarcode.FORMAT_AZTEC
            FirebaseVisionBarcode.FORMAT_CODABAR,
            FirebaseVisionBarcode.FORMAT_CODE_128,
            FirebaseVisionBarcode.FORMAT_CODE_39,
            FirebaseVisionBarcode.FORMAT_CODE_93,
            FirebaseVisionBarcode.FORMAT_UPC_A,
            FirebaseVisionBarcode.FORMAT_UPC_E,
            FirebaseVisionBarcode.FORMAT_EAN_13,
            FirebaseVisionBarcode.FORMAT_EAN_8,
            FirebaseVisionBarcode.FORMAT_ITF
        )
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@HomeFragment
            vm = homeViewModel


        }
        binding.button3.visibility = View.GONE
        binding.buttonCountToList.isClickable = false

        //unused if user account , the add to list function on
//        FirebaseAuth.getInstance().addAuthStateListener {
//            if(FirebaseAuth.getInstance().currentUser==null){
//                binding.spinner1.visibility = View.GONE
//                binding.button2.visibility = View.GONE
//            }else{
//                binding.spinner1.visibility = View.VISIBLE
//                binding.button2.visibility = View.VISIBLE
//            }
//        }

        binding.cameraView.apply {
            setLifecycleOwner(this@HomeFragment);
            addCameraListener(object : CameraListener() {

            })
        }

        var counter = 0;
        binding.cameraView.addFrameProcessor(FrameProcessor { frame ->

            Log.d("HomeFragment", "FrameProcessor")
            val newTime: Long = frame.getTime()
            val delay: Long = newTime - lastTime

            lastTime = newTime
//        LOG.e("Frame delayMillis:", delay, "FPS:", 1000 / delay)
            if (counter < 15) {
                counter += 1

                Log.d("HomeFragment", "counting")
            } else {
                // Log.d("HomeFragment", "image")
                if (frame.getFormat() === ImageFormat.NV21
                    && frame.getDataClass() === ByteArray::class.java
                ) {
                    Log.d("HomeFragment", "barcode scan")
                    val data: ByteArray = frame.getData()
                    val yuvImage = YuvImage(
                        data,
                        frame.getFormat(),
                        frame.getSize().getWidth(),
                        frame.getSize().getHeight(),
                        null
                    )
                    val jpegStream = ByteArrayOutputStream()
                    yuvImage.compressToJpeg(
                        Rect(
                            0, 0,
                            frame.getSize().getWidth(),
                            frame.getSize().getHeight()
                        ), 100, jpegStream
                    )
                    val jpegByteArray: ByteArray = jpegStream.toByteArray()
                    val bitmap = BitmapFactory.decodeByteArray(
                        jpegByteArray,
                        0, jpegByteArray.size
                    )

                    val image = FirebaseVisionImage.fromBitmap(bitmap)

                    val detector = FirebaseVision.getInstance()
                        .visionBarcodeDetector

                    val result = detector.detectInImage(image)
                        .addOnSuccessListener { barcodes ->
                            // Task completed successfully
                            if (barcodes.size > 0) {
                              //  Log.d("HomeFragment", "barcode scan success")
                                val contents = barcodes.get(0).rawValue

//                                val contents = "001"
                                Log.d("HomeFragment", "barcode scan success "+ contents.toString())
                                val docRef = db.collection("Product1")
                                    .document(contents.toString())
//                                Log.d("HomeFragment", "barcode scan success "+ docRef.path)
//                                if (docRef.get()==null){
//                                    homeViewModel.product.value = "The product is not in the database, \n " +
//                                            "we welcome you to add it to database. "
//                                    binding.button3.visibility = View.VISIBLE
//                                }
                                docRef.addSnapshotListener { snapshot, e ->

                                    if (e != null) {
                                        Log.w(TAG, "Listen failed. ${e}")
                                        homeViewModel.product.value = "The product is not in the database, \n " +
                                                "we welcome you to add it to database. "
                                       // binding.button3.visibility = View.VISIBLE
//                                        binding.apply {
//                                            button3.setOnClickListener{
//                                                    view -> findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavAddnewitem())
//                                            }
//                                        }

                                        binding.buttonCountToList.isClickable = false
                                        return@addSnapshotListener
                                    }
                                    if(snapshot==null||!snapshot?.exists()!!){
                                        Log.w(TAG, "DB no data")
                                        homeViewModel.product.value = "The product is not in the database, \n " +
                                                "we welcome you to add it to database. "
                                        homeViewModel.productnu.value=""
                                        homeViewModel.productin.value=""
                                        homeViewModel.reminder.value=""
                                        binding.button3.visibility = View.VISIBLE
                                        binding.buttonCountToList.isClickable = false
                                    }


                                    snapshot?.exists()?.let {
                                        if (!it) return@addSnapshotListener
                                        binding.button3.visibility = View.GONE
                                        homeViewModel.reminder.value=""
                                        Log.d("HomeFragment", "find record")
                                        homeViewModel.product.value = snapshot?.getString("name")
//            viewModel?.text?.value = snapshot?.getString("ingre")
                                        homeViewModel.productin.value =
                                            (snapshot?.get("ingre") as List<String>).joinToString(
                                                separator = ", "
                                            ) { it -> "${it}" }

                                        snapshot?.reference?.collection("nutri")
                                            ?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                                                homeViewModel.productnu.value =
                                                    querySnapshot?.documents?.map { item ->
                                                        "${item.id}: ${item.getLong("value")} ${item.getString("unit")}"
                                                       // Log.d("HomeFragment",item.id)

                                                    }?.joinToString(", \n")


                                                //                                                        if(item.id=="sugar"&&item.getLong("value")!!.compareTo(10.0)>1){
//                                                            homeViewModel.reminder.value="This product has high amount of sugar. \n"
//                                                        }
                                            }


                                        var reminder=""
                                        var reminderIN= listOf<String>()
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
                                            ) { it -> "${it}" }.contains("Barley")){
//                                          homeViewModel.reminder.value="Ingredients may cause allergy:\n"
                                            reminder="Ingredients may cause allergy:\n"


                                        }
                                            if ((snapshot?.get("ingre") as List<String>).joinToString(
                                                separator = ","
                                            ) { it -> "${it}" }.contains("milk")||
                                                (snapshot?.get("ingre") as List<String>).joinToString(
                                                    separator = ","
                                                ) { it -> "${it}" }.contains("Milk")){
                                               // homeViewModel.reminder.value=homeViewModel.reminder.value.plus("milk")
                                                reminderIN=reminderIN.plus("milk")
                                            }

                                        if ((snapshot?.get("ingre") as List<String>).joinToString(
                                                separator = ","
                                            ) { it -> "${it}" }.contains("egg")||
                                            (snapshot?.get("ingre") as List<String>).joinToString(
                                                separator = ","
                                            ) { it -> "${it}" }.contains("Egg")){
                                            //homeViewModel.reminder.value=homeViewModel.reminder.value.plus("egg")
                                            reminderIN=reminderIN.plus("egg")
                                        }
                                        if ((snapshot?.get("ingre") as List<String>).joinToString(
                                                separator = ","
                                            ) { it -> "${it}" }.contains("wheat")||
                                            (snapshot?.get("ingre") as List<String>).joinToString(
                                                separator = ","
                                            ) { it -> "${it}" }.contains("Wheat")){
                                           // homeViewModel.reminder.value=homeViewModel.reminder.value.plus("wheat")
                                            reminderIN=reminderIN.plus("wheat")
                                        }
                                        if ((snapshot?.get("ingre") as List<String>).joinToString(
                                                separator = ","
                                            ) { it -> "${it}" }.contains("soy")||
                                            (snapshot?.get("ingre") as List<String>).joinToString(
                                                separator = ","
                                            ) { it -> "${it}" }.contains("Soy")){
//                                            homeViewModel.reminder.value=homeViewModel.reminder.value.plus("soy")//
                                            reminderIN=reminderIN.plus("soy")
                                        }
                                        if ((snapshot?.get("ingre") as List<String>).joinToString(
                                                separator = ","
                                            ) { it -> "${it}" }.contains("nut")||
                                            (snapshot?.get("ingre") as List<String>).joinToString(
                                                separator = ","
                                            ) { it -> "${it}" }.contains("Nut")){
//                                            homeViewModel.reminder.value=homeViewModel.reminder.value.plus("nut")
                                            reminderIN=reminderIN.plus("nut")
                                        }
                                        if ((snapshot?.get("ingre") as List<String>).joinToString(
                                                separator = ","
                                            ) { it -> "${it}" }.contains("barley")||
                                            (snapshot?.get("ingre") as List<String>).joinToString(
                                                separator = ","
                                            ) { it -> "${it}" }.contains("Barley")){
//                                            homeViewModel.reminder.value=homeViewModel.reminder.value.plus("barley")
                                            reminderIN=reminderIN.plus("barley")
                                        }

                                        homeViewModel.reminder.value=reminder+" "+reminderIN.joinToString(separator = ", "){ it -> "${it}" }










                                        binding.buttonCountToList.isClickable = true


                                        binding.buttonCountToList.setOnClickListener {view->

                                            if(!homeViewModel.productnu.equals("")&&!homeViewModel.productin.equals(""))
                                            {
                                                Log.d(TAG,"adding item to user list")


//
                                                val Current = LocalDateTime.now()

                                                val nameI = homeViewModel.productnu.value!!
                                                val code = contents
                                                val data = hashMapOf<String, Any>(
//                                                    "name" to nameI,
                                                    //"date" to Current,
                                                    "updateAt" to FieldValue.serverTimestamp(),
                                                    "item" to code!!,
                                                    "timeAt" to DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(Current)
//                                                    "timeAt" to Current
                                                )
                                               //if(FirebaseAuth.getInstance().currentUser!=null){
                                                   val doc=db.collection("User").document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                                                       .collection("list").add(data).addOnCompleteListener {
                                                           Snackbar.make(
                                                               view,
                                                               "Counted to product list.",
                                                               Snackbar.LENGTH_LONG
                                                           )
                                                               .setAction("Action", null).show()

                                                       }

                                            }


//                                                    doc.addOnSuccessListener {docRef -> db.collection("User").document(FirebaseAuth.getInstance().currentUser?.uid.toString())
//                                                        .collection("list").document(docRef.id).collection("date").add(Current)
//
//                                                    }
//                                               }else{
//                                                   db.collection("User").add(FirebaseAuth.getInstance().currentUser?.uid.toString())
//                                                   db.collection("User").document(FirebaseAuth.getInstance().currentUser?.uid.toString())
//                                                   .collection("list").add(data)}

                                        }
                                    }


//                                    homeViewModel.productnu.value =
//                                        (snapshot?.get("nutri") as List<Map<String, Int>>).joinToString(
//                                            separator = ","
//                                        ) { item -> "${item.keys.joinToString(separator = ",") { "${it}" }"" }
//                                contents?.let { value ->
//                                    findNavController().navigate(
//                                        HomeFragmentDirections.actionNavHomeToNavScan(
//                                            value
//                                        )
//                                    )
////
//
//                                }
                                }
                            }
                        }
                        .addOnFailureListener {
                            // Task failed with an exception
                            // ...
                            //Log.w(TAG, "Listen failed.")
//                            homeViewModel.product.value = "The product is not in the database, \n " +
//                                    "we welcome you to add it to database. "
//                            binding.button3.visibility = View.VISIBLE
                        }

//            bitmap.toString()
                    counter = 0
                }
            }
        })
        Log.d("HomeFragment", "continue")
//        database = FirebaseDatabase.getInstance().reference
        return binding.root
    }


    //region Permissions

    //region Permissions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var valid = true
        for (grantResult in grantResults) {
            valid = valid && grantResult == PackageManager.PERMISSION_GRANTED
        }
        if (valid && !binding.cameraView.isOpened()) {
            binding.cameraView.open()
        }
    }

    //start scan app
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        val intent = Intent("com.google.zxing.client.android.SCAN")
//        intent.setPackage("com.google.zxing.client.android")
//        intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
////        startActivityForResult(intent, 0)

        binding.button3.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavAddnewitem())
        }



//        if(db.collection("User").document(FirebaseAuth.getInstance()).collection("list")){
//
//        }





    }

    //
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//
//        super.onActivityResult(requestCode, resultCode, data)
//        //after receive result
//        if (requestCode == 0) {
//            if (resultCode == Activity.RESULT_OK) {
//               // val contents = data?.getStringExtra("SCAN_RESULT")
//                val contents="001"
//                val format = data?.getStringExtra("SCAN_RESULT_FORMAT")
//                // Handle successful scan
//                //pass string to scan
//                Log.d("HomeFragment", "${format}")
//
//                contents?.let { value ->
//                    findNavController().navigate(HomeFragmentDirections.actionNavHomeToNavScan(value))
//                }
//
//            } else if (resultCode == Activity.RESULT_CANCELED) { // Handle cancel
//                //use vm.test2 show fail on home frag
//
//            }
//        }
//    }
//    fun onClickAddToListButton() {
//        //onActivityCreated()
//
//    }
}