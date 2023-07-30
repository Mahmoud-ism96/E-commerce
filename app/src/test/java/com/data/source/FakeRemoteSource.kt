package com.data.source

import com.example.e_commerce.model.pojo.*
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
import com.example.e_commerce.model.pojo.order_details_response.OrderDetailsResponse
import com.example.e_commerce.model.pojo.order_response.OrderResponse
import com.example.e_commerce.model.pojo.pricerule.PriceRuleResponse
import com.example.e_commerce.model.pojo.product_details.ProductDetailsResponse
import com.example.e_commerce.services.network.RemoteSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class FakeRemoteSource(
    private val productsResponse: ProductsResponse,
    private val brandsResponse: BrandsResponse,
    private val priceRuleResponse: PriceRuleResponse,
    private val productDetailsResponse: ProductDetailsResponse,
    private val customerResponse: CustomerResponse,
    private val customerModifiedResponse: CustomerModifiedResponse,
    private val addressResponse: AddressResponse,
    private val orderResponse: OrderResponse,
    private val customerOrderResponse: CustomerOrderResponse,
    private val orderDetailsResponse: OrderDetailsResponse,
    private val inventoryLevelResponse: InventoryLevelResponse,
    private val draftResponse: DraftResponse,
) : RemoteSource {

    override suspend fun getAllProducts(): Flow<Response<ProductsResponse>> {
        return flowOf(Response.success(productsResponse))
    }

    override suspend fun getBrand(): Flow<Response<BrandsResponse>> {
        return flowOf(Response.success(brandsResponse))
    }

    override suspend fun getAllPricesRules(): Flow<Response<PriceRuleResponse>> {
        return flowOf(Response.success(priceRuleResponse))
    }

    override suspend fun getProductsById(id: Long): Flow<Response<ProductsResponse>> {
        return flowOf(Response.success(productsResponse))
    }

    override suspend fun getProductById(productID: Long): Flow<Response<ProductDetailsResponse>> {
        return flowOf(Response.success(productDetailsResponse))
    }

    override suspend fun createCustomer(customerData: CustomerData): Flow<Response<CustomerResponse>> {
        return flowOf(Response.success(customerResponse))
    }

    override suspend fun getCustomerByEmailAndName(
        email: String,
        name: String
    ): Flow<Response<CustomerResponse>> {
        return flowOf(Response.success(customerResponse))
    }

    override suspend fun modifyCustomer(
        customerId: Long,
        customer: CustomerData
    ): Flow<Response<CustomerModifiedResponse>> {
        return flowOf(Response.success(customerModifiedResponse))
    }

    override suspend fun getAddressesForCustomer(customer_id: String): Flow<Response<AddressResponse>> {
        return flowOf(Response.success(addressResponse))
    }

    override suspend fun createAddressForCustomer(
        customer_id: String,
        sendAddress: SendAddressDTO
    ): Flow<Response<AddressResponse>> {
        return flowOf(Response.success(addressResponse))
    }

    override suspend fun makeAddressDefault(
        customer_id: String,
        address_id: String
    ): Flow<Response<AddressResponse>> {
        return flowOf(Response.success(addressResponse))
    }

    override suspend fun deleteAddressForCustomer(customer_id: String, address_id: String) {

    }

    override suspend fun createOrder(order: OrderData): Flow<Response<OrderResponse>> {
        return flowOf(Response.success(orderResponse))
    }

    override suspend fun getCustomerOrders(id: Long): Flow<Response<CustomerOrderResponse>> {
        return flowOf(Response.success(customerOrderResponse))
    }

    override suspend fun getOrderById(id: Long): Flow<Response<OrderDetailsResponse>> {
        return flowOf(Response.success(orderDetailsResponse))
    }

    override suspend fun updateInventoryLevel(inventoryLevel: InventoryLevelData): Flow<Response<InventoryLevelResponse>> {
        return flowOf(Response.success(inventoryLevelResponse))
    }

    override suspend fun createDraftOrder(draft_order: SendDraftRequest): Flow<Response<DraftResponse>> {
        return flowOf(Response.success(draftResponse))
    }

    override suspend fun modifyDraftOrder(
        draft_order_id: Long,
        draft_order: SendDraftRequest
    ): Flow<Response<DraftResponse>> {
        return flowOf(Response.success(draftResponse))
    }

    override suspend fun getDraftOrderByDraftId(draft_order_id: Long): Flow<Response<DraftResponse>> {
        return flowOf(Response.success(draftResponse))
    }
}
