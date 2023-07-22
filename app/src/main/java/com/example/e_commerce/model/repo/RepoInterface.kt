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
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepoInterface {
    suspend fun getAllProducts(): Flow<Response<ProductsResponse>>
    suspend fun getBrands(): Flow<Response<BrandsResponse>>
    suspend fun getProductsById(brandId: Long): Flow<Response<ProductsResponse>>
    suspend fun getProductById(productId: Long): Flow<Response<ProductDetailsResponse>>
    suspend fun getProductsByTitle(title: String): Flow<Response<ProductsResponse>>
    suspend fun createCustomer(customerData: CustomerData): Flow<Response<CustomerResponse>>
    suspend fun getCustomerByEmailAndName(
        email: String, name: String
    ): Flow<Response<CustomerResponse>>
    suspend fun getDiscountCodesForPriceRule(priceRuleId: String): Flow<Response<DiscountResponse>>
    suspend fun getAllPricesRules(): Flow<Response<PriceRuleResponse>>
    suspend fun insertItem(item: CartItem)
    suspend fun deleteItem(item: CartItem)
    suspend fun deleteItemById(itemId: Long)
    suspend fun updateQuantity(itemId: Long, newQuantity: Int)
    fun getAllCartItems(): Flow<List<CartItem>>
    suspend fun getAddressesForCustomer(customer_id: String): Flow<Response<AddressResponse>>
    suspend fun createAddressForCustomer(customer_id: String, sendAddress: SendAddressDTO): Flow<Response<AddressResponse>>
    suspend fun makeAddressDefault(customer_id: String, address_id: String): Flow<Response<AddressResponse>>
    suspend fun deleteAddressForCustomer(customer_id: String, address_id: String)

}