package com.example.e_commerce.services.network

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.ProductsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

object ConcreteRemoteSource: RemoteSource {
    override suspend fun getAllProducts(): Flow<Response<ProductsResponse>> {
        val productsResponse = ApiClient.apiService.getAllProducts()
        return flowOf(productsResponse)
    }

    override suspend fun getBrand(): Flow<Response<BrandsResponse>> {
        val brandsResponse=ApiClient.apiService.getBrands()
        return flowOf(brandsResponse)
    }

    override suspend fun getProductsById(id: Long): Flow<Response<ProductsResponse>> {
        val productsResponseByBrand=ApiClient.apiService.getProductsById(id)
        return flowOf(productsResponseByBrand)
    }
}