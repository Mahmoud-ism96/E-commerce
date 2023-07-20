package com.example.e_commerce.services.network

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

object ConcreteRemoteSource : RemoteSource {
    override suspend fun getAllProducts(): Flow<Response<ProductsResponse>> {
        val productsResponse = ApiClient.apiService.getAllProducts()
        return flowOf(productsResponse)
    }

    override suspend fun getBrand(): Flow<Response<BrandsResponse>> {
        val brandsResponse = ApiClient.apiService.getBrands()
        return flowOf(brandsResponse)
    }

    override suspend fun getProductsByBrand(brandId: Long): Flow<Response<ProductsResponse>> {
        val productsResponseByBrand = ApiClient.apiService.getProductsByBrand(brandId)
        return flowOf(productsResponseByBrand)
    }

    override suspend fun getProductsByTitle(title: String): Flow<Response<ProductsResponse>> {
        val productsResponseByTitle = ApiClient.apiService.getProductsByTitle(title)
        return flowOf(productsResponseByTitle)
    }

    override suspend fun createCustomer(customerData: CustomerData): Flow<Response<CustomerResponse>> {
        val createdCustomer = ApiClient.apiService.createCustomer(customerData)
        return flowOf(createdCustomer)
    }

    override suspend fun getCustomerByEmailAndName(
        email: String, name: String
    ): Flow<Response<CustomerResponse>> {
        val customerByEmailAndName = ApiClient.apiService.getCustomerByEmailAndName(email, name)
        return flowOf(customerByEmailAndName)
    }
}