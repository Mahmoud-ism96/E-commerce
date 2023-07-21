package com.example.e_commerce.profile.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.databinding.FragmentAddressBookBinding
import com.example.e_commerce.model.pojo.address.AddressResponse
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.profile.viewmodel.SettingViewModel
import com.example.e_commerce.profile.viewmodel.SettingViewModelFactory
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.shoppingcart.view.TAG
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AddressBookFragment : Fragment() {

    private lateinit var settingViewModel: SettingViewModel
    private lateinit var mAuth: FirebaseAuth
    override fun onStart() {
        super.onStart()
        val homeActivity = requireActivity() as HomeActivity
        homeActivity.binding.bottomNavigationBar.visibility = View.GONE
    }

    private lateinit var binding: FragmentAddressBookBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddressBookBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        val factory = SettingViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource, ConcreteLocalSource.getInstance(requireContext())
            )
        )

        settingViewModel =
            ViewModelProvider(requireActivity(), factory)[SettingViewModel::class.java]
        mAuth.currentUser?.let {
            settingViewModel.getCurrentCustomer(it.email!!, it.displayName!!)
        }

        lifecycleScope.launch {
            settingViewModel.currentCustomerStateFlow.collectLatest {
                when (it) {
                    is ApiState.Loading -> {}
                    is ApiState.Success -> {
                        Log.w(TAG, "onViewCreated:done   ${(it.data as CustomerResponse).customer}" )
                    }

                    is ApiState.Failure -> {}
                }
            }
        }

        binding.btnAddAddress.setOnClickListener {
            val action =
                AddressBookFragmentDirections.actionAccountDetailsFragmentToAddAddressFragment()
            findNavController().navigate(action)
        }




    }

    override fun onDestroy() {
        super.onDestroy()
        val homeActivity = requireActivity() as HomeActivity
        homeActivity.binding.bottomNavigationBar.visibility = View.VISIBLE
    }

}


//binding.btnMap.setOnClickListener {
//            val navController = Navigation.findNavController(view)
//            navController.navigate(R.id.action_accountDetailsFragment_to_mapFragment)
//        }
//
//        binding.btnBackAccountDetails.setOnClickListener {
//            findNavController().popBackStack()
//        }
//        binding.btnSave.setOnClickListener {
//
//        }