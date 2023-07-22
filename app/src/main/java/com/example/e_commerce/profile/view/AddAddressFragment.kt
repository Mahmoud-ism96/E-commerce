package com.example.e_commerce.profile.view

import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.databinding.FragmentAddAddressBinding
import com.example.e_commerce.model.pojo.address.Coordinate
import com.example.e_commerce.model.pojo.address.SendAddress
import com.example.e_commerce.model.pojo.address.SendAddressDTO
import com.example.e_commerce.model.pojo.customer_resposnse.Customer
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.profile.viewmodel.SettingViewModel
import com.example.e_commerce.profile.viewmodel.SettingViewModelFactory
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AddAddressFragment : Fragment() {

    private lateinit var binding: FragmentAddAddressBinding
    private lateinit var settingViewModel: SettingViewModel
    private lateinit var currentCustomer: Customer
    private var coordinate: Coordinate? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAddressBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        coordinate = AddAddressFragmentArgs.fromBundle(requireArguments()).coordinate
        if(coordinate != null){
            fillViewsByGeoCoder()
        }
        val factory = SettingViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource, ConcreteLocalSource.getInstance(requireContext())
            )
        )

        settingViewModel =
            ViewModelProvider(requireActivity(), factory)[SettingViewModel::class.java]

        lifecycleScope.launch {
            settingViewModel.currentCustomerStateFlow.collectLatest {
                when (it) {
                    is ApiState.Loading -> {}
                    is ApiState.Success -> {
                        currentCustomer = (it.data as CustomerResponse).customers[0]
                    }

                    is ApiState.Failure -> {}
                }
            }
        }

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
                val validationText = isAddressValidate()
                if (validationText == "") {
                    val newAddress = SendAddress(
                        address1 = etAddress.text.toString(),
                        address2 = etAdditionalInfo.text.toString(),
                        city = etCity.text.toString(),
                        company = "And Team1",
                        first_name = etFirstName.text.toString(),
                        name = etLastName.text.toString(),
                        phone = etPhoneNumber.text.toString(),
                        country = "Egypt",
                        zip = etZipCode.text.toString(),
                        country_code = "eg",
                        country_name = "Egypt",
                    )
                    settingViewModel.createAddressForCustomer(
                        currentCustomer.id.toString(),
                        SendAddressDTO(newAddress)
                    )
                    findNavController().popBackStack()
                }else{
                    Toast.makeText(requireContext(), validationText, Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun fillViewsByGeoCoder() {
        val addresses = Geocoder(requireContext()).getFromLocation(
            coordinate!!.latitude,
            coordinate!!.longitude,
            5
        )

        if (addresses != null) {
            if(addresses.isNotEmpty()){
                val address = addresses[0]
                val cityName = address.adminArea
                val fullAddress = address.getAddressLine(0)

                binding.etCity.setText(cityName)
                binding.etAddress.setText(fullAddress)
            }
        }


    }

    private fun isAddressValidate(): String {
        var validateText = ""
        if (binding.etFirstName.text.toString().isBlank()) {
            return "First Name field is Required"
        } else if ((binding.etFirstName.text?.length ?: 3) <= 2 || (binding.etFirstName.text?.length
                ?: 3) >= 10
        ) {
            return "Enter Valid First Name"
        }

        if (binding.etLastName.text.toString().isBlank()) {
            return "Last Name field is Required"
        } else if ((binding.etLastName.text?.length ?: 3) <= 2 || (binding.etLastName.text?.length
                ?: 3) >= 10
        ) {
            return "Enter Valid Last Name"
        }

        if (binding.etCity.text.toString().isBlank()) {
            return "City field is Required"
        } else if ((binding.etCity.text?.length ?: 3) <= 2 || (binding.etCity.text?.length
                ?: 3) >= 15
        ) {
            return "Enter Valid city Name"
        }

        if (binding.etAddress.text.toString().isBlank()) {
            return "Address field is Required"
        } else if ((binding.etAddress.text?.length ?: 3) <= 2) {
            return "Enter Valid address"
        }

        if (binding.etPhoneNumber.text.toString().isBlank()) {
            return "Phone Number field is Required"
        } else if ((binding.etPhoneNumber.text?.length ?: 11) <= 10) {
            return "Enter Valid Phone Number"
        }

        return validateText
    }
}