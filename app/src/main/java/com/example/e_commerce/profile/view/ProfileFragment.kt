package com.example.e_commerce.profile.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvAccountDetails.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_profileFragment2_to_accountDetailsFragment)
        }

        binding.btnOpenLanguage.setOnClickListener {
            if(binding.cvLanguage.visibility == View.VISIBLE){
                binding.cvLanguage.visibility = View.GONE
            }else{
                binding.cvLanguage.visibility = View.VISIBLE
            }
        }

        binding.tvLogOut.setOnClickListener {
            TODO("Navigate To Register screen")
        }

        binding.tvOrderDetails.setOnClickListener {
            TODO("Navigate To orders screen")
        }
    }
}