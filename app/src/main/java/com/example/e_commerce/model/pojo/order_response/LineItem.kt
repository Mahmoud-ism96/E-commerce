package com.example.e_commerce.model.pojo.order_response

data class LineItem(
    val id: Long,
    val admin_graphql_api_id: String,
    val fulfillable_quantity: Int,
    val fulfillment_service: String,
    val fulfillment_status: String,
    val grams: Int,
    val name: String,
    val price: String,
    val price_set: MoneySet,
    val product_id: Long,
    val product_exists: Boolean,
    val quantity: Int,
    val sku: String?,
    val taxable: Boolean,
    val title: String,
    val total_discount: String,
    val total_discount_set: MoneySet,
    val variant_id: String?,
    val variant_title: String?,
    val vendor: String?
)
