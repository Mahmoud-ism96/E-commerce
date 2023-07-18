package com.example.e_commerce.services.network

import com.example.e_commerce.model.ProductsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

object ConcreteRemoteSource: RemoteSource {
    override suspend fun getAllProducts(): Flow<Response<ProductsResponse>> {
        val productsResponse = ApiClient.apiService.getAllProducts()
        return flowOf(productsResponse)
    }
}