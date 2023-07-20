package com.example.e_commerce.shoppingcart.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentCartBinding
import com.example.e_commerce.model.pojo.CartItem
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.shoppingcart.viewmodel.CartViewModel
import com.example.e_commerce.shoppingcart.viewmodel.CartViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val TAG = "hassankamal"

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = CartViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource,
                ConcreteLocalSource.getInstance(requireContext())
            )
        )
        cartViewModel = ViewModelProvider(this, factory)[CartViewModel::class.java]

        cartAdapter = CartAdapter(onPlusClick = { id, quantity->
            val newQuantity = quantity + 1
            cartViewModel.updateQuantityForItem(id, newQuantity)
        }, onMinusClick = { id, quantity ->
            if (quantity > 1) {
                val newQuantity = quantity - 1
                cartViewModel.updateQuantityForItem(id, newQuantity)
            }else{
                cartViewModel.deleteItemFromCart(id)
            }
        })

        binding.rvCartItems.adapter = cartAdapter
        cartViewModel.getAllShoppingItem()


        binding.btnCheckout.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_cartFragment2_to_checkoutFragment)
        }



        lifecycleScope.launch {
            cartViewModel.cartItemsStateFlow.collectLatest {
                when (it) {
                    is ApiState.Loading -> {Log.w(TAG, "loading:", )}
                    is ApiState.Success -> {
                        cartAdapter.submitList(it.data as List<CartItem>)
                    }

                    is ApiState.Failure -> {Log.w(TAG, "error:", )}
                }
            }
        }


    }
}