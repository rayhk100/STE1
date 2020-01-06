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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.frame.FrameProcessor
//import jdk.nashorn.internal.objects.NativeDate.getTime
import java.io.ByteArrayOutputStream


class HomeFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var camera: CameraView
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
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@HomeFragment
            vm = homeViewModel
        }
        camera = binding.cameraView;
        camera.setLifecycleOwner(this);
        camera.addCameraListener(object : CameraListener() {

        })
        var counter = 0;
        camera.addFrameProcessor(FrameProcessor { frame ->

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
                                Log.d("HomeFragment", "barcode scan success")
//                      val contents =  barcodes.get(0).rawValue

                                val contents = "001"

                                val docRef = db.collection("Product1")
                                    .document(contents)
                                docRef.addSnapshotListener { snapshot, e ->
                                    if (e != null) {
                                        Log.w(TAG, "Listen failed.", e)
                                        return@addSnapshotListener
                                    }

                                    Log.d("HomeFragment", "find record")
                                    homeViewModel.product.value = snapshot?.getString("name")
//            viewModel?.text?.value = snapshot?.getString("ingre")
                                    homeViewModel.productnu.value =
                                        (snapshot?.get("ingre") as List<String>).joinToString(
                                            separator = ","
                                        ) { it -> "${it}" }
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
                        }

//            bitmap.toString()
                    counter = 0
                }
            }
        })
        Log.d("HomeFragment", "continue")
        database = FirebaseDatabase.getInstance().reference
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
        if (valid && !camera.isOpened()) {
            camera.open()
        }
    }

    //start scan app
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val intent = Intent("com.google.zxing.client.android.SCAN")
        intent.setPackage("com.google.zxing.client.android")
//        intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
//        startActivityForResult(intent, 0)
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
//    fun onClickButtonS(){
//        //onActivityCreated()
//
//    }
}