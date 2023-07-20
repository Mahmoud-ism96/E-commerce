package com.example.e_commerce.services.network

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteSource {
    suspend fun getAllProducts(): Flow<Response<ProductsResponse>>
    suspend fun getBrand(): Flow<Response<BrandsResponse>>
    suspend fun getProductsByBrand(brandId: Long): Flow<Response<ProductsResponse>>
    suspend fun getProductsByTitle(title: String): Flow<Response<ProductsResponse>>
    suspend fun createCustomer(customerData: CustomerData): Flow<Response<CustomerResponse>>
    suspend fun getCustomerByEmailAndName(email: String, name: String): Flow<Response<CustomerResponse>>
}