package com.example.e_commerce.services.currency

import com.example.e_commerce.model.pojo.Currency.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiCurrencyService {

    @GET("convert")
    suspend fun getLatestExchangeRates(
        @Query("apikey") apikey: String ,
        @Query("amount") amount: String,
        @Query("from") from: String ,
        @Query("to") to: String) : CurrencyResponse
}