package com.example.e_commerce.listOfProduct.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.e_commerce.Home.viewmodel.HomeViewModel
import com.example.e_commerce.Home.viewmodel.HomeViewModelFactory
import com.example.e_commerce.HomeActivity
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentListOfProductBinding
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListOfProductFragment : Fragment() {

    private lateinit var binding:FragmentListOfProductBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var productRecycleAdapter: ProductRecycleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentListOfProductBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModelFactory = HomeViewModelFactory(Repo.getInstance(ConcreteRemoteSource, ConcreteLocalSource.getInstance(requireContext())))
        homeViewModel =
            ViewModelProvider(requireActivity(), homeViewModelFactory)[HomeViewModel::class.java]

        productRecycleAdapter= ProductRecycleAdapter(requireContext())
        binding.rvListOfProduct.apply {
            adapter=productRecycleAdapter
            layoutManager = LinearLayoutManager(view.context).apply {
                orientation = RecyclerView.VERTICAL
            }
        }

        (Glide.with(requireContext())
            .load(homeViewModel.brandImage)
            .apply(RequestOptions().override(200, 200))
            .placeholder(R.drawable.loading_svgrepo_com)
            .error(R.drawable.error)
            .into(binding.ivBrandList))

        lifecycleScope.launch {
            homeViewModel.productsByBrandStateFlow.collectLatest {
                when(it){
                    is ApiState.Loading ->{
                        binding.rvListOfProduct.visibility=View.GONE
                        binding.listOfProductLoading.visibility=View.VISIBLE
                        binding.listOfProductLoading.setAnimation(R.raw.loading)
                    }
                    is ApiState.Success ->{
                        val product=it.data as ProductsResponse
                        productRecycleAdapter.submitList(product.products)
                        binding.rvListOfProduct.visibility=View.VISIBLE
                        binding.listOfProductLoading.visibility=View.GONE
                    }
                    else -> {
                        Toast.makeText(requireContext(),"Failed To Get Data",Toast.LENGTH_LONG).show()
                    }
                }
            }
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
}