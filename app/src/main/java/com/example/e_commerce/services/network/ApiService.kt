package com.example.e_commerce.services.network

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.pojo.address.AddressResponse
import com.example.e_commerce.model.pojo.address.SendAddressDTO
import com.example.e_commerce.model.pojo.coupons.DiscountResponse
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.pojo.customer_modified_response.CustomerModifiedResponse
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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("admin/api/2023-07/products.json")
    suspend fun getAllProducts(): Response<ProductsResponse>

    @GET("admin/api/2023-07/smart_collections.json")
    suspend fun getBrands(): Response<BrandsResponse>

    @GET("admin/api/2023-07/products.json")
    suspend fun getProductsById(@Query("collection_id") collectionId: Long): Response<ProductsResponse>

    @GET("/admin/api/2023-07/products/{product_id}.json")
    suspend fun getProductById(@Path("product_id") productID: Long): Response<ProductDetailsResponse>

    @GET("/admin/api/2023-07/price_rules.json")
    suspend fun getAllPricesRules(): Response<PriceRuleResponse>

    @GET("/admin/api/2023-07/price_rules/{price_rule_id}/discount_codes.json")
    suspend fun getDiscountCodesForPriceRule(@Path("price_rule_id") priceRuleId: String): Response<DiscountResponse>

    @POST("admin/api/2023-07/customers.json")
    suspend fun createCustomer(@Body customer: CustomerData): Response<CustomerResponse>

    @PUT("admin/api/2023-07/customers/{customerId}.json")
    suspend fun modifyCustomer(
        @Path("customerId") customerId: Long, @Body customer: CustomerData
    ): Response<CustomerModifiedResponse>

    @GET("admin/api/2023-07/customers.json")
    suspend fun getCustomerByEmailAndName(
        @Query("email") email: String, @Query("first_name") name: String
    ): Response<CustomerResponse>

    @POST("/admin/api/2023-07/customers/{customer_id}/addresses.json")
    suspend fun createAddressForCustomer(
        @Path("customer_id") customer_id: String, @Body customer_address: SendAddressDTO
    ): Response<AddressResponse>

    @GET("/admin/api/2023-07/customers/{customer_id}/addresses.json")
    suspend fun getAddressesForCustomer(@Path("customer_id") customer_id: String): Response<AddressResponse>

    @PUT("/admin/api/2023-07/customers/{customer_id}/addresses/{address_id}/default.json")
    suspend fun makeAddressDefault(
        @Path("customer_id") customer_id: String, @Path("address_id") address_id: String
    ): Response<AddressResponse>

    @DELETE("/admin/api/2023-07/customers/{customer_id}/addresses/{address_id}.json")
    suspend fun deleteAddressForCustomer(
        @Path("customer_id") customer_id: String, @Path("address_id") address_id: String
    )

    @POST("admin/api/2023-07/orders.json")
    suspend fun createOrder(@Body order: OrderData): Response<OrderResponse>

    @GET("admin/api/2023-07/customers/{customer_id}/orders.json")
    suspend fun getCustomerOrders(@Path("customer_id") id: Long): Response<OrderResponse>

    @GET("admin/api/2023-04/orders/{order_id}.json?")
    suspend fun getOrderById(@Path("order_id") id: Long): Response<Order>

    @POST("/admin/api/2023-04/inventory_levels/set.json")
    suspend fun updateInventoryLevel(@Body inventoryLevel: InventoryLevelData): Response<InventoryLevelResponse>

    @POST("/admin/api/2023-07/draft_orders.json")
    suspend fun createDraftOrder(@Body draft_order: SendDraftRequest): Response<DraftResponse>

    @PUT("/admin/api/2023-07/draft_orders/{draft_order_id}.json")
    suspend fun modifyDraftOrder(
        @Path("draft_order_id") draft_order_id: Long, @Body draft_order: SendDraftRequest
    ): Response<DraftResponse>

    @GET("/admin/api/2023-07/draft_orders/{draft_order_id}.json")
    suspend fun getDraftOrderByDraftId(@Path("draft_order_id") draft_order_id: Long): Response<DraftResponse>


}

