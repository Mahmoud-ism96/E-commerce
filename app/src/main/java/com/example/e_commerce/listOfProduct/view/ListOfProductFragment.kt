package com.example.e_commerce.listOfProduct.view

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.e_commerce.Home.viewmodel.HomeViewModel
import com.example.e_commerce.Home.viewmodel.HomeViewModelFactory
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.R
import com.example.e_commerce.databinding.DialogFilterBinding
import com.example.e_commerce.databinding.FragmentListOfProductBinding
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.utility.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListOfProductFragment : Fragment() {

    private lateinit var binding: FragmentListOfProductBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var productRecycleAdapter: ProductRecycleAdapter
    private lateinit var filterDialog: Dialog
    private lateinit var filterBinding: DialogFilterBinding
    private var type: String = ""
    private var fromPrice: Double = 0.0
    private var toPrice: Double = 10000.0
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentListOfProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        homeViewModelFactory = HomeViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource, ConcreteLocalSource.getInstance(requireContext())
            )
        )

        homeViewModel =
            ViewModelProvider(requireActivity(), homeViewModelFactory)[HomeViewModel::class.java]

        productRecycleAdapter = ProductRecycleAdapter {
            val action =
                ListOfProductFragmentDirections.actionListOfProductFragmentToProductDetailsFragment(
                    it.id
                )
            findNavController().navigate(action)
        }
        binding.rvListOfProduct.apply {
            adapter = productRecycleAdapter
            layoutManager = LinearLayoutManager(view.context).apply {
                orientation = RecyclerView.VERTICAL
            }
        }

        Glide.with(requireContext()).load(homeViewModel.brandImage)
            .apply(RequestOptions().override(400, 350)).placeholder(R.drawable.loading_svgrepo_com)
            .error(R.drawable.error).into(binding.ivBrandList)

        filterDialog = Dialog(requireContext())
        filterDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        filterBinding = DialogFilterBinding.inflate(layoutInflater)
        filterDialog.setContentView(filterBinding.root)

        binding.btnFilter.setOnClickListener {
            filterDialog.show()
        }




        lifecycleScope.launch {
            homeViewModel.productsByIdStateFlow.collectLatest {
                when (it) {
                    is ApiState.Loading -> {
                        binding.rvListOfProduct.visibility = View.GONE
                        binding.listOfProductLoading.visibility = View.VISIBLE
                        binding.listOfProductLoading.setAnimation(R.raw.loading)
                    }

                    is ApiState.Success -> {
                        val productsResponse = it.data as ProductsResponse
                        productRecycleAdapter.submitList(productsResponse.products)
                        binding.rvListOfProduct.visibility = View.VISIBLE
                        binding.listOfProductLoading.visibility = View.GONE

                        val prices = productsResponse.products.flatMap { product ->
                            product.variants.mapNotNull { variant ->
                                variant.price.toDoubleOrNull()
                            }
                        }

                        val minPrice = prices.minOrNull() ?: 0.0
                        val maxPrice = prices.maxOrNull() ?: 0.0


                        filterBinding.etFromPrice.hint=minPrice.toString()
                        filterBinding.etToPrice.hint=maxPrice.toString()

                    }

                    else -> {
                        Toast.makeText(requireContext(), "Failed To Get Data", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
        }

        filterBinding.rgType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                filterBinding.rbtnShoes.id -> {
                    type = "SHOES"
                }

                filterBinding.rbtnShirt.id -> {
                    type = "T-SHIRTS"
                }

                filterBinding.rbtnAccessories.id -> {
                    type = "ACCESSORIES"
                }
            }
        }

        filterBinding.btnRemoveFilter.setOnClickListener {
            type = ""
            fromPrice = 0.0
            toPrice = 1000.0
            lifecycleScope.launch {
                homeViewModel.productsByIdStateFlow.collectLatest {
                    when (it) {
                        is ApiState.Success -> {
                            val productsResponse = it.data as ProductsResponse
                            productRecycleAdapter.submitList(productsResponse.products)
                        }

                        else -> {}
                    }
                }
            }
            filterBinding.apply {
                rbtnShoes.isChecked = false
                rbtnAccessories.isChecked = false
                rbtnShirt.isChecked = false
                etFromPrice.text.clear()
                etToPrice.text.clear()
            }

            filterDialog.dismiss()
        }


        filterBinding.btnSaveFilter.setOnClickListener {
            lifecycleScope.launch {
                homeViewModel.productsByIdStateFlow.collectLatest {
                    when (it) {
                        is ApiState.Success -> {
                            val productsResponse = it.data as ProductsResponse
                            val products = productsResponse.products.filter { product ->
                                if (type != "") {
                                    product.product_type == type
                                } else {
                                    true
                                }

                            }


                            val filteredProducts = products.filter { product ->
                                val fromPriceText = filterBinding.etFromPrice.text.toString()
                                val toPriceText = filterBinding.etToPrice.text.toString()
                                if (fromPriceText.isNotEmpty() && toPriceText.isNotEmpty()) {
                                    fromPrice = fromPriceText.toDouble()
                                    toPrice = toPriceText.toDouble()
                                    product.variants[0].price.toDouble() in fromPrice..toPrice
                                } else {
                                    true
                                }
                            }
                            productRecycleAdapter.submitList(filteredProducts)
                        }

                        else -> {}
                    }
                }


            }
            filterDialog.dismiss()
        }

        binding.btnBackHome.setOnClickListener {
            findNavController().popBackStack()
        }

    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as HomeActivity).bottomNavigationBar.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as HomeActivity).bottomNavigationBar.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::productRecycleAdapter.isInitialized) productRecycleAdapter.submitList(emptyList())
    }
}