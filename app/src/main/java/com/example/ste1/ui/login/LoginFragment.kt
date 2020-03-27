package com.example.ste1.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.ste1.R
import com.example.ste1.databinding.LoginFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_fragment.*

class LoginFragment : Fragment() {
    private lateinit var binding: LoginFragmentBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LoginFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@LoginFragment
            vm = ViewModelProviders.of(this@LoginFragment).get(LoginViewModel::class.java)
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            // later we add here code for navigating to signup page

                loginButton.setOnClickListener { view ->
                    if (vm?.email?.value.isNullOrEmpty() || vm?.password?.value.isNullOrEmpty()) {
                        Snackbar.make(
                            view,
                            "Please input email and password.",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("Action", null).show()
                    } else {
                        FirebaseAuth.getInstance()
                            .signInWithEmailAndPassword(vm?.email?.value!!, vm?.password?.value!!)
                            .addOnCompleteListener { auth ->
                                if (auth.isSuccessful) {
                                    Snackbar.make(
                                        view,
                                        "Welcome back ${auth.result?.user?.displayName}",
                                        Snackbar.LENGTH_LONG
                                    )
                                        .setAction("Action", null).show()
                                    findNavController().navigate(R.id.nav_profile)
                                } else {
                                    Snackbar.make(
                                        view,
                                        "Please input an valid email and password",
                                        Snackbar.LENGTH_LONG
                                    )
                                        .setAction("Action", null).show()
                                }
                            }
                    }
                }
                login_signup.setOnClickListener { view ->
                    findNavController().navigate(R.id.nav_signup)

                }


        }
    }

}