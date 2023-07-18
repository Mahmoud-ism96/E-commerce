package com.example.e_commerce.shoppingcart.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentCartBinding
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.shoppingcart.viewmodel.CartViewModel
import com.example.e_commerce.shoppingcart.viewmodel.CartViewModelFactory

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    private lateinit var cartViewModel: CartViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = CartViewModelFactory(Repo.getInstance(ConcreteRemoteSource))
        cartViewModel = ViewModelProvider(this, factory)[CartViewModel::class.java]

        binding.btnCheckout.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigate(R.id.action_cartFragment2_to_checkoutFragment)
        }


    }
}