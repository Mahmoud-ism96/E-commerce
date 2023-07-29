package com.example.e_commerce

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.e_commerce.Home.viewmodel.HomeViewModel
import com.example.e_commerce.Home.viewmodel.HomeViewModelFactory
import com.example.e_commerce.databinding.ActivityHomeBinding
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.example.e_commerce.utility.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    lateinit var bottomNavigationBar: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationBar = binding.bottomNavigationBar
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottomNavigationBar, navController)


        homeViewModelFactory = HomeViewModelFactory(
            Repo.getInstance(
                ConcreteRemoteSource,
                ConcreteLocalSource.getInstance(this)
            )
        )
        homeViewModel =
            ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]

        lifecycleScope.launch(Dispatchers.IO) {
            homeViewModel.getBrands()
            homeViewModel.getPriceRules()
        }

        lifecycleScope.launch {
            homeViewModel.usdAmountStateFlow.collectLatest {
                homeViewModel.writeToSP(Constants.USDAMOUNT, it.toString())
            }
        }
    }
}