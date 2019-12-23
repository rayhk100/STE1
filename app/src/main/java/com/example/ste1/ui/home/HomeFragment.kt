package com.example.ste1.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.ste1.R
import com.example.ste1.databinding.FragmentHomeBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class HomeFragment : Fragment() {
    private lateinit var database: DatabaseReference
    private lateinit var homeViewModel: HomeViewModel



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
        database = FirebaseDatabase.getInstance().reference
        return binding.root
    }
//start scan app
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val intent = Intent("com.google.zxing.client.android.SCAN")
        intent.setPackage("com.google.zxing.client.android")
//        intent.putExtra("SCAN_MODE", "QR_CODE_MODE")
        startActivityForResult(intent, 0)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        //after receive result
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                val contents = data?.getStringExtra("SCAN_RESULT")
                val format = data?.getStringExtra("SCAN_RESULT_FORMAT")
                // Handle successful scan
                //pass string to scan
                contents
            } else if (resultCode == Activity.RESULT_CANCELED) { // Handle cancel
                //use vm.test2 show fail on home frag
            }
        }


        //go to "scan" result page
    }
}