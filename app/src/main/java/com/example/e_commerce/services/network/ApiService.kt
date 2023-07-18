package com.example.e_commerce.services.network

import com.example.e_commerce.model.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("admin/api/2023-07/products.json")
    suspend fun getAllProducts(): Response<ProductsResponse>

}