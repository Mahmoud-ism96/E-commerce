package com.example.e_commerce.profile.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentAccountDetailsBinding


class AccountDetailsFragment : Fragment() {

    override fun onStart() {
        super.onStart()
        val homeActivity = requireActivity() as HomeActivity
        homeActivity.binding.bottomNavigationBar.visibility = View.GONE
    }

    private lateinit var binding: FragmentAccountDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnMap.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_accountDetailsFragment_to_mapFragment)
        }

        binding.btnBackAccountDetails.setOnClickListener {
            navigateBack()
        }
        binding.btnSave.setOnClickListener {
            navigateBack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val homeActivity = requireActivity() as HomeActivity
        homeActivity.binding.bottomNavigationBar.visibility = View.VISIBLE
    }

    private fun navigateBack(){
        val fragmentManager = parentFragmentManager
        fragmentManager.beginTransaction().commit()
        fragmentManager.popBackStack()
    }

}