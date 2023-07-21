package com.example.e_commerce.product_details.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.e_commerce.databinding.FragmentProductDetailsBinding
import com.example.e_commerce.model.pojo.CartItem
import com.example.e_commerce.model.pojo.product_details.ProductDetailsResponse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.product_details.viewmodel.ProductDetailsViewModel
import com.example.e_commerce.product_details.viewmodel.ProductDetailsViewModelFactory
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductDetailsFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailsBinding

    private lateinit var _viewModelFactory: ProductDetailsViewModelFactory
    private lateinit var _viewModel: ProductDetailsViewModel

    private lateinit var cartItem: CartItem

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(layoutInflater, container, false)

        _viewModelFactory = ProductDetailsViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource, ConcreteLocalSource.getInstance(requireContext())
            )
        )
        _viewModel = ViewModelProvider(
            requireActivity(), _viewModelFactory
        )[ProductDetailsViewModel::class.java]

        val productID = ProductDetailsFragmentArgs.fromBundle(requireArguments()).productId

        _viewModel.getProductDetails(productID)

        lifecycleScope.launch {
            _viewModel.productDetailsMutableState.collectLatest {
                when (it) {
                    is ApiState.Loading -> {
                    }

                    is ApiState.Success -> {
                        val productDetails: ProductDetailsResponse =
                            it.data as ProductDetailsResponse

                        val title = productDetails.product.title
                        val vendor = productDetails.product.vendor
                        val price = productDetails.product.variants[0].price
                        val inventoryQuantity =
                            productDetails.product.variants[0].inventory_quantity
                        val image = productDetails.product.image.src

                        cartItem = CartItem(productID, title, price, inventoryQuantity, image)
                        binding.apply {
                            tvDetailVendorName.text = vendor
                            tvDetailProductName.text = title
                            tvDetailPrice.text = price
                        }
                    }

                    else -> {
                    }
                }
            }
        }

        binding.btnDetailAddToCart.setOnClickListener {
            if (::cartItem.isInitialized) _viewModel.addToCart(cartItem)
        }

        binding.btnDetailBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}