package com.example.e_commerce.Home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.e_commerce.Home.viewmodel.HomeViewModel
import com.example.e_commerce.Home.viewmodel.HomeViewModelFactory
import com.example.e_commerce.R
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.network.ApiState
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.utility.Constants
import com.google.android.material.carousel.CarouselLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    private lateinit var brandRecycleAdapter: BrandRecycleAdapter
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        homeViewModelFactory = HomeViewModelFactory(Repo.getInstance(ConcreteRemoteSource))
        homeViewModel =
            ViewModelProvider(requireActivity(), homeViewModelFactory)[HomeViewModel::class.java]

        brandRecycleAdapter = BrandRecycleAdapter(requireContext()) {
            homeViewModel.getProductById(it.id)
            homeViewModel.getBrandImg(it.image.src)
            navController.navigate(R.id.action_homeFragment_to_listOfProductFragment)
        }
        binding.rvBrands.apply {
            adapter = brandRecycleAdapter
            layoutManager = CarouselLayoutManager()
        }

        binding.apply {

            cvMen.setOnClickListener {
                homeViewModel.getProductById(Constants.PRODUCT_BY_MEN)
                homeViewModel.getBrandImg(R.drawable.menshoping)
                navController.navigate(R.id.action_homeFragment_to_listOfProductFragment)
            }
            cvWomen.setOnClickListener {
                homeViewModel.getProductById(Constants.PRODUCT_BY_WOMEN)
                homeViewModel.getBrandImg(R.drawable.womenshoping)
                navController.navigate(R.id.action_homeFragment_to_listOfProductFragment)
            }
            cvKids.setOnClickListener {
                homeViewModel.getProductById(Constants.PRODUCT_BY_KIDS)
                homeViewModel.getBrandImg(R.drawable.kidsshoping)
                navController.navigate(R.id.action_homeFragment_to_listOfProductFragment)
            }
            cvSale.setOnClickListener {
                homeViewModel.getProductById(Constants.PRODUCT_BY_SALE)
                homeViewModel.getBrandImg(R.drawable.saleshoping)
                navController.navigate(R.id.action_homeFragment_to_listOfProductFragment)
            }

        }

        lifecycleScope.launch {
            homeViewModel.brandsStateFlow.collectLatest {

                when (it) {
                    is ApiState.Loading -> {
                        binding.apply {
                            btnSearch.visibility = View.GONE
                            rvOffer.visibility = View.GONE
                            cvKids.visibility = View.GONE
                            cvMen.visibility = View.GONE
                            cvWomen.visibility = View.GONE
                            cvSale.visibility = View.GONE
                            textView10.visibility = View.GONE
                            textView11.visibility = View.GONE
                            textView12.visibility = View.GONE
                            textView14.visibility = View.GONE
                            textView9.visibility = View.GONE
                            rvBrands.visibility = View.GONE
                            loading.visibility = View.VISIBLE
                            loading.setAnimation(R.raw.loading)
                        }
                    }

                    is ApiState.Success -> {
                        binding.apply {
                            loading.visibility = View.GONE
                            btnSearch.visibility = View.VISIBLE
                            rvOffer.visibility = View.VISIBLE
                            cvKids.visibility = View.VISIBLE
                            cvMen.visibility = View.VISIBLE
                            cvWomen.visibility = View.VISIBLE
                            cvSale.visibility = View.VISIBLE
                            textView10.visibility = View.VISIBLE
                            textView11.visibility = View.VISIBLE
                            textView12.visibility = View.VISIBLE
                            textView14.visibility = View.VISIBLE
                            textView9.visibility = View.VISIBLE
                            rvBrands.visibility = View.VISIBLE

                        }
                        val brandsResponse: BrandsResponse = it.data as BrandsResponse
                        brandRecycleAdapter.submitList(brandsResponse.smart_collections)
                    }

                    else -> {
                        Toast.makeText(requireContext(), "Failed To Get Data", Toast.LENGTH_LONG)
                            .show()
                    }
                }

            }
        }

    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            homeViewModel.productsByIdStateFlow.emit(ApiState.Loading)
        }
    }

}