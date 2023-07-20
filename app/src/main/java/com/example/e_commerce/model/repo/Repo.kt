package com.example.e_commerce.model.repo

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.CartItem
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.pojo.coupons.DiscountResponse
import com.example.e_commerce.model.pojo.pricerule.PriceRuleResponse
import com.example.e_commerce.services.db.LocalSource
import com.example.e_commerce.services.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class Repo private constructor(private val remoteSource: RemoteSource, private val localSource: LocalSource): RepoInterface {

    companion object{
        private var instance: Repo? = null

        fun getInstance(remoteSource: RemoteSource, localSource: LocalSource): Repo {
            return instance ?: synchronized(this){
                instance ?: Repo(remoteSource, localSource).also { instance = it }
            }
        }
    }

    override suspend fun getAllProducts(): Flow<Response<ProductsResponse>> {
        return remoteSource.getAllProducts()
    }

    override suspend fun getBrands(): Flow<Response<BrandsResponse>> {
        return remoteSource.getBrand()
    }

    override suspend fun getProductsById(brandId: Long): Flow<Response<ProductsResponse>> {
        return remoteSource.getProductsById(brandId)
    }

    override suspend fun getDiscountCodesForPriceRule(priceRuleId: String): Flow<Response<DiscountResponse>> {
        return remoteSource.getDiscountCodesForPriceRule(priceRuleId)
    }

    override suspend fun getAllPricesRules(): Flow<Response<PriceRuleResponse>> {
        return remoteSource.getAllPricesRules()
    }


    override suspend fun insertItem(item: CartItem) {
        localSource.insertItem(item)
    }

    override suspend fun deleteItem(item: CartItem) {
        localSource.deleteItem(item)
    }

    override suspend fun deleteItemById(itemId: Long) {
        localSource.deleteItemById(itemId)
    }

    override suspend fun updateQuantity(itemId: Long, newQuantity: Int) {
        localSource.updateQuantity(itemId, newQuantity)
    }

    override fun getAllCartItems(): Flow<List<CartItem>> {
        return localSource.getAllCartItems()
    }

}