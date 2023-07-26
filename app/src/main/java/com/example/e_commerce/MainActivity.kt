package com.example.e_commerce

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.databinding.ActivityMainBinding
import com.example.e_commerce.services.currency.ApiCurrencyClient
import com.example.e_commerce.services.db.ConcreteLocalSource
import com.example.e_commerce.services.settingsharedpreference.SettingSharedPref
import com.example.e_commerce.utility.Constants
import com.example.e_commerce.utility.Functions
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val settingSharedPref=SettingSharedPref.getInstance(this)
        if (settingSharedPref.readStringFromSettingSP(Constants.CURRENCY).isNullOrBlank()){
            settingSharedPref.writeStringToSettingSP(Constants.CURRENCY,Constants.EGP)
        }
    }
}