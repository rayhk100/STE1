package com.example.ste1.ui.scan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.ste1.databinding.ScanFragmentBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore


class ScanFragment : Fragment() {
val TAG = "ScanFragment"
    val db = FirebaseFirestore.getInstance()
//    val options = FirebaseVisionBarcodeDetectorOptions.Builder()
//        .setBarcodeFormats(
//            FirebaseVisionBarcode.FORMAT_QR_CODE,
//            FirebaseVisionBarcode.FORMAT_AZTEC,
//            FirebaseVisionBarcode.FORMAT_EAN_13
//        )
//        .build()
//    var image = FirebaseVisionImage.fromBitmap(bitmap);

    

    companion object {
        fun newInstance() = ScanFragment()
    }
//    private lateinit var database: DatabaseReference
    private lateinit var viewModel: ScanViewModel
    private lateinit var binding:ScanFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
        ViewModelProviders.of(this).get(ScanViewModel::class.java)
        val binding = ScanFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@ScanFragment
            vm2 = viewModel


        }
//        binding.code = arguments?.getString("code")


//        database = FirebaseDatabase.getInstance().reference
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProviders.of(this).get(ScanViewModel::class.java)
        // TODO: Use the ViewModel

        val docRef = db.collection("Product1").document(arguments?.getString("code")!!)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }


            viewModel?.name?.value=snapshot?.getString("name")
//            viewModel?.text?.value = snapshot?.getString("ingre")
            viewModel?.ingre.value = (snapshot?.get("ingre") as List<String>).joinToString(separator = ",") { it -> "${it}" }

//            val source = if (snapshot != null && snapshot.metadata.hasPendingWrites())
//                "Local"
//            else
//                "Server"
//
//            if (snapshot != null && snapshot.exists()) {
//                Log.d(TAG, "$source data: ${snapshot.data}")
//            } else {
//                Log.d(TAG, "$source data: null")
//            }
        }
    }
  // get firebase data
    //show data
    // have function to add to list
    



//    private class YourImageAnalyzer : ImageAnalysis.Analyzer {
//        private fun degreesToFirebaseRotation(degrees: Int): Int = when(degrees) {
//            0 -> FirebaseVisionImageMetadata.ROTATION_0
//            90 -> FirebaseVisionImageMetadata.ROTATION_90
//            180 -> FirebaseVisionImageMetadata.ROTATION_180
//            270 -> FirebaseVisionImageMetadata.ROTATION_270
//            else -> throw Exception("Rotation must be 0, 90, 180, or 270.")
//        }
//
//        override fun analyze(imageProxy: ImageProxy?, degrees: Int) {
//            val mediaImage = imageProxy?.image
//            val imageRotation = degreesToFirebaseRotation(degrees)
//            if (mediaImage != null) {
//                val image = FirebaseVisionImage.fromMediaImage(mediaImage, imageRotation)
//                // Pass image to an ML Kit Vision API
//                // ...
//            }
//        }
//    }

}
