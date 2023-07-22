package com.example.e_commerce.model.repo

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.CartItem
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.pojo.address.AddressResponse
import com.example.e_commerce.model.pojo.address.SendAddress
import com.example.e_commerce.model.pojo.address.SendAddressDTO
import com.example.e_commerce.model.pojo.coupons.DiscountResponse
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import com.example.e_commerce.model.pojo.pricerule.PriceRuleResponse
import com.example.e_commerce.model.pojo.product_details.ProductDetailsResponse
import com.example.e_commerce.services.db.LocalSource
import com.example.e_commerce.services.network.ApiClient
import com.example.e_commerce.services.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class Repo private constructor(
    private val remoteSource: RemoteSource, private val localSource: LocalSource
) : RepoInterface {

    companion object {
        private var instance: Repo? = null

        fun getInstance(remoteSource: RemoteSource, localSource: LocalSource): Repo {
            return instance ?: synchronized(this) {
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

    override suspend fun getProductById(productId: Long): Flow<Response<ProductDetailsResponse>> {
        return remoteSource.getProductById(productId)
    }

    override suspend fun getProductsByTitle(title: String): Flow<Response<ProductsResponse>> {
        return remoteSource.getProductsByTitle(title)
    }

    override suspend fun createCustomer(customerData: CustomerData): Flow<Response<CustomerResponse>> {
        return remoteSource.createCustomer(customerData)
    }

    override suspend fun getCustomerByEmailAndName(
        email: String, name: String
    ): Flow<Response<CustomerResponse>> {
        return remoteSource.getCustomerByEmailAndName(email, name)
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

    override suspend fun getAddressesForCustomer(customer_id: String): Flow<Response<AddressResponse>> {
        return remoteSource.getAddressesForCustomer(customer_id)
    }

    override suspend fun createAddressForCustomer(
        customer_id: String,
        sendAddress: SendAddressDTO
    ): Flow<Response<AddressResponse>> {
        return remoteSource.createAddressForCustomer(customer_id, sendAddress)
    }

    override suspend fun makeAddressDefault(
        customer_id: String,
        address_id: String
    ): Flow<Response<AddressResponse>> {
        return remoteSource.makeAddressDefault(customer_id, address_id)
    }

    override suspend fun deleteAddressForCustomer(customer_id: String, address_id: String) {
        remoteSource.deleteAddressForCustomer(customer_id, address_id)
    }
}