package com.example.e_commerce.services.network

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.pojo.coupons.DiscountResponse
import com.example.e_commerce.model.pojo.pricerule.PriceRuleResponse
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

    override suspend fun getProductsByBrand(brandId: Long): Flow<Response<ProductsResponse>> {
        val productsResponseByBrand=ApiClient.apiService.getProductsByBrand(brandId)
        return flowOf(productsResponseByBrand)
    }

    override suspend fun getDiscountCodesForPriceRule(priceRuleId: String): Flow<Response<DiscountResponse>> {
        return flowOf(ApiClient.apiService.getDiscountCodesForPriceRule(priceRuleId))
    }

    override suspend fun getAllPricesRules(): Flow<Response<PriceRuleResponse>> {
        return flowOf(ApiClient.apiService.getAllPricesRules())
    }

}