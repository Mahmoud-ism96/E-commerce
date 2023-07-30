package com.example.e_commerce.search.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentSearchBinding
import com.example.e_commerce.listOfProduct.view.ProductRecycleAdapter
import com.example.e_commerce.model.pojo.Brand
import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.Product
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.search.viewmodel.SearchViewModel
import com.example.e_commerce.search.viewmodel.SearchViewModelFactory
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.utility.Functions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    private lateinit var productAdapter: ProductRecycleAdapter
    private lateinit var productHintAdapter: ProductHintAdapter

    private lateinit var productList: List<Product>
    private lateinit var brandListHint: List<Brand>

    private lateinit var _viewModelFactory: SearchViewModelFactory
    private lateinit var _viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        _viewModelFactory = SearchViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource, ConcreteLocalSource.getInstance(requireContext())
            )
        )
        _viewModel = ViewModelProvider(
            requireActivity(), _viewModelFactory
        )[SearchViewModel::class.java]

        productList = emptyList()
        brandListHint = emptyList()

        if(Functions.checkConnectivity(requireContext())){
            _viewModel.getAllProducts()
            _viewModel.getAllBrands()
        }else {
            (requireActivity() as HomeActivity).noConnectionGroup.visibility = View.VISIBLE
            (requireActivity() as HomeActivity).retryButton.setOnClickListener {
                if(Functions.checkConnectivity(requireContext())){
                    _viewModel.getAllProducts()
                    _viewModel.getAllBrands()
                    (requireActivity() as HomeActivity).noConnectionGroup.visibility = View.GONE
                }else {
                    Toast.makeText(requireContext(), getString(R.string.couldn_t_retrieve_data), Toast.LENGTH_SHORT).show()
                }
            }
        }

        lifecycleScope.launch {
            _viewModel.productList.collectLatest {
                when (it) {
                    is ApiState.Success -> {
                        val productResponse = it.data as ProductsResponse
                        productList = productResponse.products
                        binding.flLoadingSearch.visibility = View.GONE
                    }

                    is ApiState.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.error_loading_data_please_try_again_later),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ApiState.Loading -> {
                        binding.flLoadingSearch.visibility = View.VISIBLE
                    }
                }
            }
        }

        lifecycleScope.launch {
            _viewModel.brandHint.collectLatest {
                when (it) {
                    is ApiState.Success -> {
                        val brandResponse = it.data as BrandsResponse
                        brandListHint = brandResponse.smart_collections
                        binding.flLoadingSearch.visibility = View.GONE
                    }

                    is ApiState.Failure -> {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.error_loading_data_please_try_again_later),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ApiState.Loading -> {
                        binding.flLoadingSearch.visibility = View.VISIBLE
                    }
                }
            }
        }

        productAdapter = ProductRecycleAdapter {
            val action = SearchFragmentDirections.actionSearchFragmentToProductDetailsFragment(
                it.id
            )
            findNavController().navigate(action)

        }

        binding.rvSearch.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
            }
        }

        productHintAdapter = ProductHintAdapter() {
            binding.searchBar.text = it
            binding.searchview.hide()
        }

        binding.rvSearchHint.apply {
            adapter = productHintAdapter
            layoutManager = LinearLayoutManager(requireContext()).apply {
                orientation = RecyclerView.VERTICAL
            }
        }

        binding.searchBar.textView.addTextChangedListener { editable ->
            val searchText = editable.toString().trim()
            val filteredList = productList.filter { product ->
                product.title.contains(searchText, ignoreCase = true)
            }
            productAdapter.submitList(filteredList)
        }

        binding.searchview.editText.addTextChangedListener { editable ->
            val searchText = editable.toString().trim()
            val filteredList = brandListHint.filter { brand ->
                brand.body_html.contains(searchText, ignoreCase = true)
            }
            productHintAdapter.submitList(filteredList)
        }

        binding.searchview
            .getEditText()
            .setOnEditorActionListener { v, actionId, event ->
                binding.searchBar.setText(binding.searchview.getText())
                binding.searchview.hide()
                false
            }


        return binding.root
    }
}