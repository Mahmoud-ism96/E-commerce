package com.example.e_commerce.authentication.startup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentStartupBinding
import com.example.e_commerce.intro.MyIntro
import com.google.firebase.auth.FirebaseAuth

class StartupFragment : Fragment() {

    private lateinit var binding: FragmentStartupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartupBinding.inflate(layoutInflater, container, false)

        val mAuth = FirebaseAuth.getInstance()

        if (mAuth.currentUser != null) {
            if (mAuth.currentUser!!.isEmailVerified) {
                if (mAuth.currentUser!!.displayName != null) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.welcome) + " " + mAuth.currentUser!!.displayName,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                val intent = Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

        }

        binding.btnStartupToSignup.setOnClickListener {
            findNavController().navigate(R.id.action_startupFragment_to_signUpFragment)
        }

        binding.btnStartupToSignin.setOnClickListener {
            findNavController().navigate(R.id.action_startupFragment_to_signInFragment)
        }

        binding.btnContinueAsGuest.setOnClickListener {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}