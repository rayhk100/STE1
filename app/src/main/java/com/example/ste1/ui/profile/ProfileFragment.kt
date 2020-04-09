package com.example.ste1.ui.profile

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import coil.api.load

import com.example.ste1.R
import com.example.ste1.databinding.ProfileFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var binding: ProfileFragmentBinding
    val REQUEST_GALLERY_CODE = 1

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
                val avatarImg=FirebaseAuth.getInstance().currentUser?.photoUrl
                avatar.value = avatarImg.toString()
//                avatar.value =R.drawable.avatar.toString()
                binding.profileAvatar.load(avatarImg)
//                binding.profileAvatar.setImageURI(avatarImg)
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



                val req = UserProfileChangeRequest.Builder().setDisplayName(displayName.value)
                FirebaseStorage.getInstance().getReference("${email?.value}/avatar.png").putFile(
                    Uri.parse(avatar.value)).addOnSuccessListener { task ->
                    FirebaseStorage.getInstance().getReference("${email?.value}/avatar.png").downloadUrl.addOnSuccessListener { downloadUri ->
                        FirebaseAuth.getInstance().currentUser?.updateProfile(req.setPhotoUri(downloadUri).build())
                    }
                }

            }


        }
        binding.profileSelectAvatar.setOnClickListener { view ->
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
//            Log.d("Sign_up"," on click")
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY_CODE)

        }

        binding.profileLogout.setOnClickListener { view ->
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.nav_home)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            REQUEST_GALLERY_CODE -> {
                data?.data?.let { uri ->
                    Log.d("Sign_up",uri.toString())
                    binding.profileAvatar.setImageURI(uri)
//                    binding.signupAvatar.load(R.drawable.avatar)
//                    Log.d("Sign_up",binding.signupAvatar.load(uri).isDisposed().toString())
                    binding.vm?.avatar?.value = uri.toString()
                }
            }
        }
    }
    fun Double.roundTo(n: Int):Double{
        return "%.${n}f".format(this).toDouble()

    }


}
