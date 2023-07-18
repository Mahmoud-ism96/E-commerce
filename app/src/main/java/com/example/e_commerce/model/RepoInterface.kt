package com.example.e_commerce.model

import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepoInterface {
    suspend fun getAllProducts(): Flow<Response<ProductsResponse>>
}