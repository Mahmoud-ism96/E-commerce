package com.example.e_commerce.services.currency

interface CurrencyRemoteSource {
    suspend fun convertCurrency(amount: String, currency: String): Double
}