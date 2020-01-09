package com.example.ste1.ui.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.ste1.R
import com.example.ste1.databinding.ProfileFragmentBinding
import com.google.firebase.auth.FirebaseAuth

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
            if (!FirebaseAuth.getInstance().currentUser?.photoUrl.toString().isNullOrEmpty()) {
                avatar.value = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()
                displayName.value = FirebaseAuth.getInstance().currentUser?.displayName
                email.value = FirebaseAuth.getInstance().currentUser?.email
            }
        }

        binding.profileLogout.setOnClickListener { view ->
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.nav_home)
        }
    }

}