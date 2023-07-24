package com.example.e_commerce.profile.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.databinding.FragmentAddressBookBinding
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import com.example.e_commerce.model.pojo.address.AddressResponse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.profile.viewmodel.SettingViewModel
import com.example.e_commerce.profile.viewmodel.SettingViewModelFactory
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class AddressBookFragment : Fragment() {

    private lateinit var settingViewModel: SettingViewModel
    private lateinit var addressRecyclerAdapter: AddressRecyclerAdapter
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

        val factory = SettingViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource, ConcreteLocalSource.getInstance(requireContext())
            )
        )
        settingViewModel =
            ViewModelProvider(requireActivity(), factory)[SettingViewModel::class.java]

        addressRecyclerAdapter = AddressRecyclerAdapter { customerId, addressId ->
            showReloadProgress()
            settingViewModel.makeAddressDefault(customerId.toString(), addressId.toString())
        }
        binding.rvAddresses.adapter = addressRecyclerAdapter
        deleteWhenSwipe()

        lifecycleScope.launch {
            settingViewModel.addressesStateFlow.collectLatest {
                when (it) {
                    is ApiState.Loading -> {}
                    is ApiState.Success -> {
                        val response = it.data as AddressResponse
                        addressRecyclerAdapter.submitList(response.addresses)
                        hideReloadProgress()
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

        binding.btnBackAccountDetails.setOnClickListener {
            findNavController().popBackStack()
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        val homeActivity = requireActivity() as HomeActivity
        homeActivity.binding.bottomNavigationBar.visibility = View.VISIBLE
    }

    private fun deleteWhenSwipe() {
        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val address = addressRecyclerAdapter.currentList[position]

                if (!address.default) {
                    settingViewModel.deleteAddressForCustomer(
                        address.customer_id.toString(),
                        address.id.toString()
                    )
                    showReloadProgress()
                    Toast.makeText(requireContext(), "address deleted ....", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "you can't delete default address",
                        Toast.LENGTH_SHORT
                    ).show()
                    addressRecyclerAdapter.notifyItemChanged(position)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallBack)
        itemTouchHelper.attachToRecyclerView(binding.rvAddresses)
    }

    private fun showReloadProgress(){
        binding.relodGroub.visibility = View.VISIBLE
    }
    private fun hideReloadProgress(){
        binding.relodGroub.visibility = View.GONE
    }

}