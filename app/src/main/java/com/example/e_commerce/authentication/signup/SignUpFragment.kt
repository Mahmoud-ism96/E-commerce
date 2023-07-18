package com.example.e_commerce.authentication.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        binding.btnSignUpToStartup.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSignup.setOnClickListener {
            //TODO: Implement Sign Up Button
            Toast.makeText(requireContext(), "Not implemented yet", Toast.LENGTH_SHORT).show()
        }

        binding.btnGmailSignup.setOnClickListener {
            //TODO: Implement Google Sign Up Button
            Toast.makeText(requireContext(), "Not implemented yet", Toast.LENGTH_SHORT).show()
        }

        binding.tvSignupToSignin.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        return binding.root
    }
}