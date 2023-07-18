package com.example.e_commerce.authentication.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(layoutInflater, container, false)

        binding.btnSignInToStartup.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnSignin.setOnClickListener {
            //TODO: Implement Sign In Button
            Toast.makeText(requireContext(), "Not implemented yet", Toast.LENGTH_SHORT).show()
        }

        binding.btnGmailSignin.setOnClickListener {
            //TODO: Implement Google Sign In Button
            Toast.makeText(requireContext(), "Not implemented yet", Toast.LENGTH_SHORT).show()
        }

        binding.tvSigninToSignup.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }

        return binding.root
    }
}