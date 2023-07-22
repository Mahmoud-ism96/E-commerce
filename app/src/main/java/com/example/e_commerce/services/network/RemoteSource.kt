package com.example.e_commerce.services.network

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.pojo.address.AddressResponse
import com.example.e_commerce.model.pojo.address.SendAddressDTO
import com.example.e_commerce.model.pojo.coupons.DiscountResponse
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import com.example.e_commerce.model.pojo.pricerule.PriceRuleResponse
import com.example.e_commerce.model.pojo.product_details.ProductDetailsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RemoteSource {
    suspend fun getAllProducts(): Flow<Response<ProductsResponse>>
    suspend fun getBrand():Flow<Response<BrandsResponse>>
    suspend fun getDiscountCodesForPriceRule(priceRuleId: String): Flow<Response<DiscountResponse>>
    suspend fun getAllPricesRules(): Flow<Response<PriceRuleResponse>>
    suspend fun getProductsById(id:Long) : Flow<Response<ProductsResponse>>
    suspend fun getProductById(productID:Long): Flow<Response<ProductDetailsResponse>>
    suspend fun getProductsByTitle(title: String): Flow<Response<ProductsResponse>>
    suspend fun createCustomer(customerData: CustomerData): Flow<Response<CustomerResponse>>
    suspend fun getCustomerByEmailAndName(email: String, name: String): Flow<Response<CustomerResponse>>
    suspend fun getAddressesForCustomer(customer_id: String): Flow<Response<AddressResponse>>
    suspend fun createAddressForCustomer(customer_id: String, sendAddress: SendAddressDTO): Flow<Response<AddressResponse>>
    suspend fun makeAddressDefault(customer_id: String, address_id: String): Flow<Response<AddressResponse>>
    suspend fun deleteAddressForCustomer(customer_id: String, address_id: String)

}

