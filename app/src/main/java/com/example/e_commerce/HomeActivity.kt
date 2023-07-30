package com.example.e_commerce

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.Group
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
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    lateinit var bottomNavigationBar: BottomNavigationView
    private lateinit var navController: NavController
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var homeViewModelFactory: HomeViewModelFactory

    lateinit var retryButton : MaterialCardView
    lateinit var noConnectionGroup : Group

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

        retryButton = binding.btnRetryConnection
        noConnectionGroup = binding.groupNoConnection

        retryButton.setOnClickListener {
            if (Functions.checkConnectivity(this)) {
                retrieveData()
                noConnectionGroup.visibility = View.GONE
            } else {
                Toast.makeText(this, getString(R.string.couldn_t_retrieve_data), Toast.LENGTH_SHORT).show()
            }
        }

        if (Functions.checkConnectivity(this)) {
            retrieveData()

            noConnectionGroup.visibility = View.GONE
        } else {
            noConnectionGroup.visibility = View.VISIBLE
        }
    }

    private fun retrieveData() {
            homeViewModel.getBrands()
            homeViewModel.getPriceRules()
            homeViewModel.convertCurrency()


        lifecycleScope.launch {
            homeViewModel.usdAmountStateFlow.collectLatest {
                homeViewModel.writeToSP(Constants.USDAMOUNT, it.toString())
            }
        }
    }
}