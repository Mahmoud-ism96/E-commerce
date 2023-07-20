package com.example.e_commerce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.e_commerce.Home.viewmodel.HomeViewModel
import com.example.e_commerce.Home.viewmodel.HomeViewModelFactory
import com.example.e_commerce.databinding.ActivityHomeBinding
import com.example.e_commerce.model.repo.Repo
import com.example.e_commerce.services.network.ConcreteRemoteSource
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    lateinit var bottomNavigationBar: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationBar = binding.bottomNavigationBar
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(bottomNavigationBar, navController)

        homeViewModelFactory = HomeViewModelFactory(Repo.getInstance(ConcreteRemoteSource))
        homeViewModel =
            ViewModelProvider(this, homeViewModelFactory)[HomeViewModel::class.java]

        homeViewModel.getBrands()

    }
}