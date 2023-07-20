package com.example.e_commerce.services.network

import com.example.e_commerce.model.pojo.BrandsResponse
import com.example.e_commerce.model.pojo.ProductsResponse
import com.example.e_commerce.model.pojo.coupons.DiscountResponse
import com.example.e_commerce.model.pojo.pricerule.PriceRuleResponse
import com.example.e_commerce.model.pojo.customer.CustomerData
import com.example.e_commerce.model.pojo.customer_resposnse.CustomerResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @GET("admin/api/2023-07/products.json")
    suspend fun getAllProducts(): Response<ProductsResponse>

    @GET("admin/api/2023-07/smart_collections.json")
    suspend fun getBrands(): Response<BrandsResponse>

    @GET("admin/api/2023-07/products.json")
    suspend fun getProductsById(@Query("collection_id") collectionId: Long):Response<ProductsResponse>

    @GET("/admin/api/2023-07/price_rules.json")
    suspend fun getAllPricesRules(): Response<PriceRuleResponse>

    @GET("/admin/api/2023-07/price_rules/{price_rule_id}/discount_codes.json")
    suspend fun getDiscountCodesForPriceRule(@Path("price_rule_id") priceRuleId: String): Response<DiscountResponse>

    @GET("admin/api/2023-07/products.json")
    suspend fun getProductsByTitle(@Query("title") title: String): Response<ProductsResponse>

    @POST("admin/api/2023-07/customers.json")
    suspend fun createCustomer(@Body customer: CustomerData): Response<CustomerResponse>

    @GET("admin/api/2023-07/customers.json")
    suspend fun getCustomerByEmailAndName(
        @Query("email") email: String, @Query("first_name") name: String
    ): Response<CustomerResponse>
}

