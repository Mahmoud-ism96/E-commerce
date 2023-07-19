package com.example.e_commerce.model.repo

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.ProductsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepoInterface {
    suspend fun getAllProducts(): Flow<Response<ProductsResponse>>
    suspend fun getBrands():Flow<Response<BrandsResponse>>
    suspend fun getProductsByBrand(brandId:Long) : Flow<Response<ProductsResponse>>
}