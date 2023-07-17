package com.example.e_commerce.authentication.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_commerce.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        binding.btnSignUpToStartup.setOnClickListener {
            TODO("Not yet implemented")
        }

        binding.btnSignup.setOnClickListener {
            TODO("Not yet implemented")
        }

        binding.btnGmailSignup.setOnClickListener {
            TODO("Not yet implemented")
        }

        return binding.root
    }
}