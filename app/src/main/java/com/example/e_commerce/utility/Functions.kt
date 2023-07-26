package com.example.e_commerce.utility

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat
import com.example.e_commerce.services.currency.ApiCurrencyClient

object Functions {
    fun checkConnectivity(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun copyToClipboard(context: Context, text: String) {
        val clipboardManager =
            ContextCompat.getSystemService(
                context,
                ClipboardManager::class.java
            ) as ClipboardManager

        val clipData = ClipData.newPlainText("keyword", text)

        clipboardManager.setPrimaryClip(clipData)
    }

    suspend fun convertCurrency(amount: String, currency: String): Double {
        if (currency!=Constants.EGP){
        return ApiCurrencyClient.convertCurrency(amount, currency)
        }else{
            return amount.toDouble()
        }
    }
}