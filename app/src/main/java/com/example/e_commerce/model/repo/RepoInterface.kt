package com.example.e_commerce.model.repo

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.CartItem
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.pojo.address.AddressResponse
import com.example.e_commerce.model.pojo.address.SendAddressDTO
import com.example.e_commerce.model.pojo.coupons.DiscountResponse
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.pojo.customer_modified_response.CustomerModifiedResponse
import com.example.e_commerce.model.pojo.customer_order_response.CustomerOrderResponse
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import com.example.e_commerce.model.pojo.draftorder.response.DraftResponse
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftRequest
import com.example.e_commerce.model.pojo.level.InventoryLevelData
import com.example.e_commerce.model.pojo.levelResponse.InventoryLevelResponse
import com.example.e_commerce.model.pojo.order.OrderData
import com.example.e_commerce.model.pojo.customer_order_response.Order
import com.example.e_commerce.model.pojo.order_response.OrderResponse
import com.example.e_commerce.model.pojo.order_details_response.OrderDetailsResponse
import com.example.e_commerce.model.pojo.pricerule.PriceRuleResponse
import com.example.e_commerce.model.pojo.product_details.ProductDetailsResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepoInterface {
    suspend fun getAllProducts(): Flow<Response<ProductsResponse>>
    suspend fun getBrands(): Flow<Response<BrandsResponse>>
    suspend fun getProductsById(brandId: Long): Flow<Response<ProductsResponse>>
    suspend fun getProductById(productId: Long): Flow<Response<ProductDetailsResponse>>
    suspend fun createCustomer(customerData: CustomerData): Flow<Response<CustomerResponse>>
    suspend fun getCustomerByEmailAndName(
        email: String, name: String
    ): Flow<Response<CustomerResponse>>

    suspend fun modifyCustomer(
        customerId: Long,
        customer: CustomerData
    ): Flow<Response<CustomerModifiedResponse>>

    suspend fun getAllPricesRules(): Flow<Response<PriceRuleResponse>>
    suspend fun getAddressesForCustomer(customer_id: String): Flow<Response<AddressResponse>>
    suspend fun createAddressForCustomer(
        customer_id: String,
        sendAddress: SendAddressDTO
    ): Flow<Response<AddressResponse>>

    suspend fun makeAddressDefault(
        customer_id: String,
        address_id: String
    ): Flow<Response<AddressResponse>>

    suspend fun deleteAddressForCustomer(customer_id: String, address_id: String)
    suspend fun createOrder(order: OrderData): Flow<Response<OrderResponse>>
    suspend fun getCustomerOrders(id: Long): Flow<Response<CustomerOrderResponse>>
    suspend fun getOrderById(id: Long): Flow<Response<OrderDetailsResponse>>
    suspend fun updateInventoryLevel(inventoryLevel: InventoryLevelData): Flow<Response<InventoryLevelResponse>>
    fun writeStringToSettingSP(key: String, value: String)
    fun readStringFromSettingSP(key: String): String
    suspend fun createDraftOrder(draft_order: SendDraftRequest): Flow<Response<DraftResponse>>
    suspend fun modifyDraftOrder(
        draft_order_id: Long,
        draft_order: SendDraftRequest
    ): Flow<Response<DraftResponse>>

    suspend fun getDraftOrderByDraftId(draft_order_id: Long): Flow<Response<DraftResponse>>
}