package com.example.e_commerce.services.network

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.pojo.address.AddressResponse
import com.example.e_commerce.model.pojo.address.SendAddressDTO
import com.example.e_commerce.model.pojo.coupons.DiscountResponse
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import com.example.e_commerce.model.pojo.draftorder.response.DraftResponse
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftRequest
import com.example.e_commerce.model.pojo.level.InventoryLevelData
import com.example.e_commerce.model.pojo.levelResponse.InventoryLevelResponse
import com.example.e_commerce.model.pojo.order.OrderData
import com.example.e_commerce.model.pojo.order_response.Order
import com.example.e_commerce.model.pojo.order_response.OrderResponse
import com.example.e_commerce.model.pojo.pricerule.PriceRuleResponse
import com.example.e_commerce.model.pojo.product_details.ProductDetailsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

object ConcreteRemoteSource : RemoteSource {
    override suspend fun getAllProducts(): Flow<Response<ProductsResponse>> {
        val productsResponse = ApiClient.apiService.getAllProducts()
        return flowOf(productsResponse)
    }

    override suspend fun getBrand(): Flow<Response<BrandsResponse>> {
        val brandsResponse = ApiClient.apiService.getBrands()
        return flowOf(brandsResponse)
    }

    override suspend fun getProductsById(id: Long): Flow<Response<ProductsResponse>> {
        val productsResponseByBrand = ApiClient.apiService.getProductsById(id)
        return flowOf(productsResponseByBrand)
    }

    override suspend fun getProductById(productID: Long): Flow<Response<ProductDetailsResponse>> {
        val productResponseByID = ApiClient.apiService.getProductById(productID)
        return flowOf(productResponseByID)
    }

    override suspend fun getDiscountCodesForPriceRule(priceRuleId: String): Flow<Response<DiscountResponse>> {
        return flowOf(ApiClient.apiService.getDiscountCodesForPriceRule(priceRuleId))
    }

    override suspend fun getAllPricesRules(): Flow<Response<PriceRuleResponse>> {
        return flowOf(ApiClient.apiService.getAllPricesRules())
    }

    override suspend fun createCustomer(customerData: CustomerData): Flow<Response<CustomerResponse>> {
        val createdCustomer = ApiClient.apiService.createCustomer(customerData)
        return flowOf(createdCustomer)
    }

    override suspend fun getCustomerByEmailAndName(
        email: String, name: String
    ): Flow<Response<CustomerResponse>> {
        val customerByEmailAndName = ApiClient.apiService.getCustomerByEmailAndName(email, name)
        return flowOf(customerByEmailAndName)
    }

    override suspend fun getAddressesForCustomer(customer_id: String): Flow<Response<AddressResponse>> {
        return flowOf(ApiClient.apiService.getAddressesForCustomer(customer_id))
    }

    override suspend fun createAddressForCustomer(
        customer_id: String,
        sendAddress: SendAddressDTO
    ): Flow<Response<AddressResponse>> {
        return flowOf(ApiClient.apiService.createAddressForCustomer(customer_id, sendAddress))
    }

    override suspend fun makeAddressDefault(
        customer_id: String,
        address_id: String
    ): Flow<Response<AddressResponse>> {
        return flowOf(ApiClient.apiService.makeAddressDefault(customer_id, address_id))
    }

    override suspend fun deleteAddressForCustomer(customer_id: String, address_id: String) {
        ApiClient.apiService.deleteAddressForCustomer(customer_id, address_id)
    }

    override suspend fun createOrder(order: OrderData): Flow<Response<OrderResponse>> {
        val orderData = ApiClient.apiService.createOrder(order)
        return flowOf(orderData)
    }

    override suspend fun getCustomerOrders(id: Long): Flow<Response<OrderResponse>> {
        val order = ApiClient.apiService.getCustomerOrders(id)
        return flowOf(order)
    }

    override suspend fun getOrderById(id: Long): Flow<Response<Order>> {
        val order = ApiClient.apiService.getOrderById(id)
        return flowOf(order)
    }

    override suspend fun updateInventoryLevel(inventoryLevel: InventoryLevelData): Flow<Response<InventoryLevelResponse>> {
        val inventory = ApiClient.apiService.updateInventoryLevel(inventoryLevel)
        return flowOf(inventory)
    }

    override suspend fun createDraftOrder(draft_order: SendDraftRequest): Flow<Response<DraftResponse>> {
        return flowOf(ApiClient.apiService.createDraftOrder(draft_order))
    }

    override suspend fun modifyDraftOrder(
        draft_order_id: Long,
        draft_order: SendDraftRequest
    ): Flow<Response<DraftResponse>> {
        return flowOf(ApiClient.apiService.modifyDraftOrder(draft_order_id, draft_order))
    }

    override suspend fun getDraftOrderByDraftId(draft_order_id: Long): Flow<Response<DraftResponse>> {
        return flowOf(ApiClient.apiService.getDraftOrderByDraftId(draft_order_id))
    }
}