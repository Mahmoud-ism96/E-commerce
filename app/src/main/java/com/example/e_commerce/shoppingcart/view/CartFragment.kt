package com.example.e_commerce.shoppingcart.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.MainActivity
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentCartBinding
import com.example.e_commerce.model.pojo.draftorder.response.DraftResponse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.shoppingcart.viewmodel.CartViewModel
import com.example.e_commerce.shoppingcart.viewmodel.CartViewModelFactory
import com.example.e_commerce.utility.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val TAG = "hassankamal"

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    private lateinit var mAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        if (mAuth.currentUser == null) {
            binding.groupSigned.visibility = View.GONE
            binding.groupNotSigned.visibility = View.VISIBLE
            binding.btnSigninSetting.setOnClickListener {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        } else {
            val factory = CartViewModelFactory(
                Repo.getInstance(
                    ConcreteRemoteSource,
                    ConcreteLocalSource.getInstance(requireContext())
                )
            )
            cartViewModel = ViewModelProvider(this, factory)[CartViewModel::class.java]

            cartAdapter = CartAdapter(onPlusClick = { _, quantity ->
               val newQuantity = quantity + 1
               // cartViewModel.updateQuantityForItem(id, newQuantity)
            }, onMinusClick = { _, quantity ->
                if (quantity > 1) {
                    val newQuantity = quantity - 1
                 //   cartViewModel.updateQuantityForItem(id, newQuantity)
                } else {
                   // cartViewModel.deleteItemFromCart(id)
                }
            }, onItemClick = {
                val action = CartFragmentDirections.actionCartFragment2ToProductDetailsFragment(it)
                findNavController().navigate(action)
            })

            binding.rvCartItems.adapter = cartAdapter

            //cartViewModel.getDraftOrderByDraftId(1128717615403)

            lifecycleScope.launch {
                cartViewModel.cartDraftOrderStateFlow.collectLatest {
                    when (it) {
                        is ApiState.Loading -> {
                            Log.w(TAG, "loading:")
                        }

                        is ApiState.Success -> {
                            createListForAdapter(it.data as DraftResponse)
                        }

                        is ApiState.Failure -> {
                            Log.w(TAG, "error:")
                        }
                    }
                }
            }

            binding.btnCheckout.setOnClickListener {
                val navController = Navigation.findNavController(view)
                navController.navigate(R.id.action_cartFragment2_to_checkoutFragment)
            }

            binding.btnApplyVoucher.setOnClickListener {
                val voucherText = binding.etVoucherCode.text.toString()
                if (!binding.etVoucherCode.text.isNullOrBlank()) {
                    if (voucherText == Constants.CODE_DISCOUNT_100) {
                        Toast.makeText(requireContext(), "congrats 100% off", Toast.LENGTH_SHORT)
                            .show()
                    } else if (voucherText == Constants.CODE_DISCOUNT_35) {
                        Toast.makeText(requireContext(), "congrats 35% off", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
    }

    private fun createListForAdapter(draftResponse: DraftResponse) {
        val lineItems = draftResponse.draft_order.line_items
        cartAdapter.submitList(lineItems)
    }
}