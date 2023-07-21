package com.example.e_commerce.profile.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.databinding.FragmentAddAddressBinding
import com.example.e_commerce.model.pojo.address.SendAddress
import com.example.e_commerce.model.pojo.customer.Addresse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.profile.viewmodel.SettingViewModel
import com.example.e_commerce.profile.viewmodel.SettingViewModelFactory
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ConcreteRemoteSource


class AddAddressFragment : Fragment() {

    private lateinit var binding: FragmentAddAddressBinding
    private lateinit var settingViewModel: SettingViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAddressBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = SettingViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource, ConcreteLocalSource.getInstance(requireContext())
            )
        )

        settingViewModel =
            ViewModelProvider(requireActivity(), factory)[SettingViewModel::class.java]

        binding.etRegion.isEnabled = false
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnMap.setOnClickListener {
            val action = AddAddressFragmentDirections.actionAddAddressFragmentToMapFragment()
            findNavController().navigate(action)
        }

        binding.apply {
            btnSave.setOnClickListener {
                val newAddress = SendAddress(
                    customer_id = 7167160058155,
                            address1 = "1 Rue des Carrieres",
                            address2 = "Suite 1234",
                            city = "Montreal",
                            company = "Fancy Co.",
                            first_name = "Samuel",
                            last_name = "de Champlain",
                            phone = "819-555-5555",
                            province = "Quebec",
                            country = "Canada",
                            zip = "G1R 4P5",
                            name = "Samuel de Champlain",
                            province_code = "QC",
                            country_code = "CA",
                            country_name = "Canada",
                )
                settingViewModel.createAddressForCustomer("7167160058155", newAddress)
            }
        }


    }
}