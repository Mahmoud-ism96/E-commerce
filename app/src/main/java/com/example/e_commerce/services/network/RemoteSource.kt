package com.example.e_commerce.services.network

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.pojo.coupons.DiscountResponse
import com.example.e_commerce.model.pojo.pricerule.PriceRuleResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteSource {
    suspend fun getAllProducts(): Flow<Response<ProductsResponse>>
    suspend fun getBrand():Flow<Response<BrandsResponse>>
    suspend fun getProductsByBrand(brandId:Long) : Flow<Response<ProductsResponse>>
    suspend fun getDiscountCodesForPriceRule(priceRuleId: String): Flow<Response<DiscountResponse>>
    suspend fun getAllPricesRules(): Flow<Response<PriceRuleResponse>>
 }