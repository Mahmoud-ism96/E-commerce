package com.example.e_commerce.profile.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentProfileBinding
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.profile.viewmodel.SettingViewModel
import com.example.e_commerce.profile.viewmodel.SettingViewModelFactory
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var mAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()
        val factory = SettingViewModelFactory(
            Repo.getInstance(
            ConcreteRemoteSource, ConcreteLocalSource.getInstance(requireContext())
        ))

        settingViewModel = ViewModelProvider(requireActivity(), factory)[SettingViewModel::class.java]

        mAuth.currentUser?.let {
            settingViewModel.getCurrentCustomer(it.email!!, it.displayName!!)
        }

        lifecycleScope.launch {
            settingViewModel.currentCustomerStateFlow.collectLatest {
                when (it) {
                    is ApiState.Loading -> {}
                    is ApiState.Success -> {
                        val response = it.data as CustomerResponse
                        settingViewModel.getAddressesForCustomer(response.customers[0].id.toString())
                    }

                    is ApiState.Failure -> {}
                }
            }
        }
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