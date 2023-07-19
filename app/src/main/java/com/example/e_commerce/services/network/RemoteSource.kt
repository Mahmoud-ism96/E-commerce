package com.example.e_commerce.services.network

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.ProductsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteSource {
    suspend fun getAllProducts(): Flow<Response<ProductsResponse>>
    suspend fun getBrand():Flow<Response<BrandsResponse>>
    suspend fun getProductsById(id:Long) : Flow<Response<ProductsResponse>>
}