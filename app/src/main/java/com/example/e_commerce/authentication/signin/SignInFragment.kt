package com.example.e_commerce.authentication.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentSignInBinding
import com.example.e_commerce.databinding.FragmentStartupBinding

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        binding.btnSignInToStartup.setOnClickListener {
            TODO("Not yet implemented")
        }

        binding.tvSigninToSignup.setOnClickListener {
            TODO("Not yet implemented")
        }

        binding.btnGmailSignin.setOnClickListener {
            TODO("Not yet implemented")
        }

        return binding.root
    }

}