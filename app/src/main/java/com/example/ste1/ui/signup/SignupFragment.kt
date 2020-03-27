package com.example.ste1.ui.signup

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.api.load
import com.example.ste1.MainActivity

import com.example.ste1.R
import com.example.ste1.databinding.SignupFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.signup_fragment.*

class SignupFragment : Fragment() {

    companion object {
        fun newInstance() = SignupFragment()
    }

    private lateinit var binding: SignupFragmentBinding
    val REQUEST_GALLERY_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignupFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@SignupFragment
            vm = ViewModelProviders.of(this@SignupFragment).get(SignupViewModel::class.java)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        (context as MainActivity).binding.appBarMain.fab.hide()


        binding.signupSelectAvatar.setOnClickListener { view ->
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY_CODE)
        }

        binding.signupSubmit.setOnClickListener { view ->
            binding.vm?.apply {
                if (password?.value!! != confirmPassword?.value!!) {
                    Snackbar.make(view, "Password dose not match.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    return@apply
                }

                if (sex.value.isNullOrEmpty()){
                    Snackbar.make(view, "Sex has not been selected.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
                    return@apply
                }
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


                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email?.value!!, password?.value!!).addOnSuccessListener { auth ->

                    val req = UserProfileChangeRequest.Builder().setDisplayName(displayName.value)

                    if (avatar.value.isNullOrEmpty()) {
                        auth.user?.updateProfile(req.build())?.addOnSuccessListener {
                            Snackbar.make(view, "Account successfully created!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show()
                            findNavController().navigate(R.id.nav_home)
                        }
                    } else {

                        FirebaseStorage.getInstance().getReference("${email?.value}/avatar.png").putFile(
                            Uri.parse(avatar.value)).addOnSuccessListener { task ->
                            FirebaseStorage.getInstance().getReference("${email?.value}/avatar.png").downloadUrl.addOnSuccessListener { downloadUri ->
                                auth.user?.updateProfile(req.setPhotoUri(downloadUri).build())?.addOnSuccessListener {
                                    Snackbar.make(view, "Account successfully created!", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show()
                                    findNavController().navigate(R.id.nav_home)
                                }
                            }
                        }
                    }

                    val data= hashMapOf<String, Any>(
                        "age" to age.value!!.toInt(),
                        "weight" to weight.value!!.toDouble(),
                        "height" to height.value!!.toDouble(),
                        "sex" to sex.value!!
                    )
                    FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().currentUser?.uid.toString())
                        .set(data)
//                        .collection("info").add(data)

//                    val dataA= hashMapOf<String, Any>("value" to age.value!!)
//                    val dataW=hashMapOf<String, Any>("value" to weight.value!!, "unit" to "kg")
//                    val dataH= hashMapOf<String, Any>("value" to height.value!!, "unit" to "cm")
//                    val dataS= hashMapOf<String,Any>("value" to sex.value!!)
//
//                    FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().currentUser?.uid.toString())
//                        .collection("info").document("age").set(dataA)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            REQUEST_GALLERY_CODE -> {
                data?.data?.let { uri ->
                    binding.signupAvatar.load(uri)
                    binding.vm?.avatar?.value = uri.toString()
                }
            }
        }
    }
    fun Double.roundTo(n: Int):Double{
        return "%.${n}f".format(this).toDouble()

    }
}