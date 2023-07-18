package com.example.e_commerce.services.network

import com.example.e_commerce.model.ProductsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteSource {
    suspend fun getAllProducts(): Flow<Response<ProductsResponse>>
}