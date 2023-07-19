package com.example.e_commerce.services.network

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.ProductsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("admin/api/2023-07/products.json")
    suspend fun getAllProducts(): Response<ProductsResponse>

    @GET("admin/api/2023-07/smart_collections.json")
    suspend fun getBrands():Response<BrandsResponse>

    @GET("admin/api/2023-07/products.json")
    suspend fun getProductsByBrand(@Query("collection_id") collectionId: Long):Response<ProductsResponse>

}