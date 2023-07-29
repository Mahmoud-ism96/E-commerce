package com.example.e_commerce.model.pojo.Currency

data class CurrencyResponse(
    val date: String,
    val historical: String,
    val info: Info,
    val query: Query,
    val result: Double,
    val success: Boolean
)