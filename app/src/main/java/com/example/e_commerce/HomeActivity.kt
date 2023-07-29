package com.example.e_commerce

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
import com.example.e_commerce.utility.Functions
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        val currency = homeViewModel.readFromSP(Constants.CURRENCY)

        binding.btnRetryConnection.setOnClickListener {
            if (Functions.checkConnectivity(this)) {
                retrieveData(currency)
                binding.groupNoConnection.visibility = View.GONE
            }else{
                Toast.makeText(this,"Couldn't retrieve data",Toast.LENGTH_SHORT).show()
            }
        }

        if (Functions.checkConnectivity(this)) {
            retrieveData(currency)

            binding.groupNoConnection.visibility = View.GONE
        } else {
            binding.groupNoConnection.visibility = View.VISIBLE
        }
    }

    private fun retrieveData(currency: String) {
        homeViewModel.getBrands()
        homeViewModel.getPriceRules()

        lifecycleScope.launch {
            val usdAmount = Functions.convertCurrency("1", "USD")
            homeViewModel.writeToSP(Constants.USDAMOUNT, usdAmount.toString())
        }

        if (currency.isNullOrBlank()) {
            homeViewModel.writeToSP(Constants.CURRENCY, Constants.EGP)
        }
    }
}