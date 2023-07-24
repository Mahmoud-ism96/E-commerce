package com.example.e_commerce.model.pojo.levelResponse

data class InventoryLevel(
    val admin_graphql_api_id: String,
    val available: Int,
    val inventory_item_id: Long,
    val location_id: Long,
    val updated_at: String
)