package com.example.e_commerce.authentication.passwordreset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.databinding.FragmentPasswordResetBinding
import com.google.firebase.auth.FirebaseAuth

class PasswordResetFragment : Fragment() {

    private lateinit var binding: FragmentPasswordResetBinding

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordResetBinding.inflate(layoutInflater, container, false)

        mAuth = FirebaseAuth.getInstance()

        binding.btnForgetBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnRecoverPassword.setOnClickListener {
            val email = binding.etRecoverEmail.text.toString()

            if (email.isNotBlank()) {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "A password reset email has been sent to your registered email address.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed to send the password reset email. Please check your email address.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }else {
                Toast.makeText(
                    requireContext(),
                    "Please enter a valid email.",
                    Toast.LENGTH_SHORT
                ).show()            }
        }
        return binding.root
    }

}