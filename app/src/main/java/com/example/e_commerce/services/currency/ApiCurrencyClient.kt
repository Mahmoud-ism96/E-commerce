package com.example.e_commerce.services.currency

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiCurrencyClient : CurrencyRemoteSource {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.apilayer.com/exchangerates_data/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: ApiCurrencyService = retrofit.create(ApiCurrencyService::class.java)

    override suspend fun convertCurrency(amount: String, currency: String): Double {
        val response = service.getLatestExchangeRates("iJE5wwtZSDqRo3gzln6ju5suoncyYMTm",amount, "EGP",currency)
        return response.result
    }
}