package com.example.e_commerce.authentication.startup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_commerce.databinding.FragmentStartupBinding

class StartupFragment : Fragment() {

    private lateinit var binding: FragmentStartupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentStartupBinding.inflate(layoutInflater, container, false)

        binding.btnStartupToSignup.setOnClickListener{
            TODO("Not yet implemented")
        }

        binding.btnStartupToSignin.setOnClickListener{
            TODO("Not yet implemented")
        }

        return binding.root
    }

}