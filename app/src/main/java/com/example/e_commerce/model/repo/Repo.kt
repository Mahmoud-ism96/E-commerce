package com.example.e_commerce.model.repo

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.CartItem
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.pojo.address.AddressResponse
import com.example.e_commerce.model.pojo.address.SendAddressDTO
import com.example.e_commerce.model.pojo.coupons.DiscountResponse
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.pojo.customer_order_response.CustomerOrderResponse
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import com.example.e_commerce.model.pojo.draftorder.response.DraftResponse
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftRequest
import com.example.e_commerce.model.pojo.level.InventoryLevelData
import com.example.e_commerce.model.pojo.levelResponse.InventoryLevelResponse
import com.example.e_commerce.model.pojo.order.OrderData
import com.example.e_commerce.model.pojo.customer_order_response.Order
import com.example.e_commerce.model.pojo.order_response.OrderResponse
import com.example.e_commerce.model.pojo.draftorder.response.DraftResponse
import com.example.e_commerce.model.pojo.draftorder.send.SendDraftRequest
import com.example.e_commerce.model.pojo.order_details_response.OrderDetailsResponse
import com.example.e_commerce.model.pojo.pricerule.PriceRuleResponse
import com.example.e_commerce.model.pojo.product_details.ProductDetailsResponse
import com.example.e_commerce.services.db.LocalSource
import com.example.e_commerce.services.network.RemoteSource
import kotlinx.coroutines.flow.Flow
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

    override suspend fun getAddressesForCustomer(customer_id: String): Flow<Response<AddressResponse>> {
        return remoteSource.getAddressesForCustomer(customer_id)
    }

    override suspend fun createAddressForCustomer(
        customer_id: String, sendAddress: SendAddressDTO
    ): Flow<Response<AddressResponse>> {
        return remoteSource.createAddressForCustomer(customer_id, sendAddress)
    }

    override suspend fun makeAddressDefault(
        customer_id: String, address_id: String
    ): Flow<Response<AddressResponse>> {
        return remoteSource.makeAddressDefault(customer_id, address_id)
    }

    override suspend fun deleteAddressForCustomer(customer_id: String, address_id: String) {
        remoteSource.deleteAddressForCustomer(customer_id, address_id)
    }

    override suspend fun createOrder(order: OrderData): Flow<Response<OrderResponse>> {
        return remoteSource.createOrder(order)
    }

    override suspend fun getCustomerOrders(id: Long): Flow<Response<CustomerOrderResponse>> {
        return remoteSource.getCustomerOrders(id)
    }

    override suspend fun getOrderById(id: Long): Flow<Response<OrderDetailsResponse>> {
        return remoteSource.getOrderById(id)
    }

    override suspend fun updateInventoryLevel(inventoryLevel: InventoryLevelData): Flow<Response<InventoryLevelResponse>> {
        return remoteSource.updateInventoryLevel(inventoryLevel)
    }

    override fun writeStringToSettingSP(key: String, value: String) {
        localSource.writeStringToSettingSP(key, value)
    }

    override fun readStringFromSettingSP(key: String): String {
        return localSource.readStringFromSettingSP(key)
    }

    override suspend fun createDraftOrder(draft_order: SendDraftRequest): Flow<Response<DraftResponse>> {
        return remoteSource.createDraftOrder(draft_order)
    }

    override suspend fun getDraftOrderByDraftId(draft_order_id: Long): Flow<Response<DraftResponse>> {
        return remoteSource.getDraftOrderByDraftId(draft_order_id)
    }
}