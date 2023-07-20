package com.example.e_commerce.model.repo

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.CartItem
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.pojo.coupons.DiscountResponse
import com.example.e_commerce.model.pojo.pricerule.PriceRuleResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepoInterface {
    suspend fun getAllProducts(): Flow<Response<ProductsResponse>>
    suspend fun getBrands():Flow<Response<BrandsResponse>>
    suspend fun getProductsById(brandId:Long) : Flow<Response<ProductsResponse>>
    suspend fun getDiscountCodesForPriceRule(priceRuleId: String): Flow<Response<DiscountResponse>>
    suspend fun getAllPricesRules(): Flow<Response<PriceRuleResponse>>

    suspend fun insertItem(item: CartItem)
    suspend fun deleteItem(item: CartItem)
    suspend fun deleteItemById(itemId: Long)
    suspend fun updateQuantity( itemId:Long, newQuantity: Int)
    fun getAllCartItems(): Flow<List<CartItem>>
}