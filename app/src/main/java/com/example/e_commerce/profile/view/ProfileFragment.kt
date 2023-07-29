package com.example.e_commerce.profile.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.e_commerce.MainActivity
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentProfileBinding
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.profile.viewmodel.SettingViewModel
import com.example.e_commerce.profile.viewmodel.SettingViewModelFactory
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.utility.Constants
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
            )
        )

        settingViewModel =
            ViewModelProvider(requireActivity(), factory)[SettingViewModel::class.java]

        if (mAuth.currentUser == null) {
            binding.groupWhenSigned.visibility = View.GONE
            binding.groupNotSigned.visibility = View.VISIBLE
            binding.btnSigninSetting.setOnClickListener {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        } else {
            setUserData()

            setDefaultRadioButton()

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

            binding.btnOpenSetting.setOnClickListener {
                if (binding.cvSetting.visibility == View.VISIBLE) {
                    binding.cvSetting.visibility = View.GONE
                } else {
                    binding.cvSetting.visibility = View.VISIBLE
                }
            }

            binding.radioGroupSettingLanguage.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == R.id.radio_setting_arabic) {
                    settingViewModel.writeStringToSettingSP(
                        Constants.LANGUAGE, Constants.ARABIC
                    )
                } else {
                    settingViewModel.writeStringToSettingSP(Constants.LANGUAGE, Constants.ENGLISH)
                }
            }

            binding.radioGroupSettingCurrency.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == R.id.radio_setting_usd) {
                    settingViewModel.writeStringToSettingSP(
                        Constants.CURRENCY, Constants.USD
                    )
                } else {
                    settingViewModel.writeStringToSettingSP(Constants.CURRENCY, Constants.EGP)
                }
            }

            binding.tvLogOut.setOnClickListener {
                if (mAuth.currentUser != null) {
                    mAuth.signOut()
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }

            binding.tvOrderDetails.setOnClickListener {
                val action = ProfileFragmentDirections.actionProfileFragment2ToOrdersFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun setUserData() {
        binding.tvProfileName.text = mAuth.currentUser!!.displayName
        binding.tvEmailNameSetting.text = mAuth.currentUser!!.email
        Glide.with(requireContext())
            .load(mAuth.currentUser!!.photoUrl)
            .apply(RequestOptions().override(200, 200))
            .placeholder(R.drawable.loading_svgrepo_com)
            .error(R.drawable.error)
            .into(binding.ivProfileImage)
    }

    private fun setDefaultRadioButton() {
        if (settingViewModel.readStringFromSettingSP(
                Constants.LANGUAGE
            ) == Constants.ARABIC
        ) {
            binding.radioGroupSettingLanguage.check(R.id.radio_setting_arabic)
        } else {
            settingViewModel.writeStringToSettingSP(Constants.LANGUAGE, Constants.ENGLISH)
            binding.radioGroupSettingLanguage.check(R.id.radio_setting_english)
        }

        if (settingViewModel.readStringFromSettingSP(
                Constants.CURRENCY
            ) == Constants.USD
        ) {
            binding.radioGroupSettingCurrency.check(R.id.radio_setting_usd)
        } else {
            settingViewModel.writeStringToSettingSP(Constants.CURRENCY, Constants.EGP)
            binding.radioGroupSettingCurrency.check(R.id.radio_setting_egp)
        }
    }
}