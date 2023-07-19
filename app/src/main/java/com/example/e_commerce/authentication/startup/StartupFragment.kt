package com.example.e_commerce.authentication.startup

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentStartupBinding
import com.example.e_commerce.intro.MyIntro

class StartupFragment : Fragment() {

    private lateinit var binding: FragmentStartupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartupBinding.inflate(layoutInflater, container, false)

        binding.btnStartupToSignup.setOnClickListener{
            findNavController().navigate(R.id.action_startupFragment_to_signUpFragment)
        }

        binding.btnStartupToSignin.setOnClickListener{
            findNavController().navigate(R.id.action_startupFragment_to_signInFragment)
        }

        binding.btnContinueAsGuest.setOnClickListener {
            //TODO: Check isFirstTime -> Send to MyIntro, else -> Send to HomeActivity
            val intent = Intent(requireContext(), MyIntro::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}