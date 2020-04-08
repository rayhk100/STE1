package com.example.ste1.ui.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController

import com.example.ste1.R
import com.example.ste1.databinding.ProfileFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var binding: ProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProfileFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@ProfileFragment
            vm = ViewModelProviders.of(this@ProfileFragment).get(ProfileViewModel::class.java)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.vm?.apply {
            val db = FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().currentUser?.uid.toString())

            if (!FirebaseAuth.getInstance().currentUser?.photoUrl.toString().isNullOrEmpty()) {
                avatar.value = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
                binding.profileAvatar.setImageURI(FirebaseAuth.getInstance().currentUser?.photoUrl)
                displayName.value = FirebaseAuth.getInstance().currentUser?.displayName
                email.value = FirebaseAuth.getInstance().currentUser?.email
                 db.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                     age.value=documentSnapshot?.get("age")?.toString()
                     weight.value=documentSnapshot?.get("weight")?.toString()
                     height.value=documentSnapshot?.get("height")?.toString()
                     sex.value=documentSnapshot?.get("sex")?.toString()
                     Log.d("Profile ",avatar.value!!.toUri().toString())
                 }


            }else{
                displayName.value = FirebaseAuth.getInstance().currentUser?.displayName
                email.value = FirebaseAuth.getInstance().currentUser?.email
                db.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                    age.value=documentSnapshot?.get("age")?.toString()
                    weight.value=documentSnapshot?.get("weight")?.toString()
                    height.value=documentSnapshot?.get("height")?.toString()
                    sex.value=documentSnapshot?.get("sex")?.toString()
//                    Log.d("Profile ",avatar.value!!.toUri().toString())
                }
            }
        }
        binding.profileUpdate.setOnClickListener{view->
            binding.vm?.apply {
                if (age.value.isNullOrEmpty()||weight.value.isNullOrEmpty()||height.value.isNullOrEmpty()){
                    Snackbar.make(view, "No input for age, weight or height.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    return@apply
                }
                else if (age.value?.toInt()!!<=0 ||weight.value?.toDouble()!!<=0||height.value?.toDouble()!!<=0){
                    Snackbar.make(view, "Invalid value for age, weight or height.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    return@apply
                }else{
                    weight.value=weight.value?.toDouble()?.roundTo(1).toString()
                    height.value=height.value?.toDouble()?.roundTo(1).toString()
                }


                val data= hashMapOf<String, Any>(
                "age" to age?.value!!.toInt(),
                "weight" to weight?.value!!.toDouble(),
                "height" to height?.value!!.toDouble(),
                "sex" to sex?.value!!
            )
                FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                    .set(data, SetOptions.merge()).addOnSuccessListener {
                        Snackbar.make(view, "Account successfully updated!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                    }

            }


        }

        binding.profileLogout.setOnClickListener { view ->
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.nav_home)
        }
    }
    fun Double.roundTo(n: Int):Double{
        return "%.${n}f".format(this).toDouble()

    }


}