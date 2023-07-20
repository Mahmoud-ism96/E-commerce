package com.example.e_commerce.model.pojo.product_details

data class Option(
    val id: Long,
    val name: String,
    val position: Int,
    val product_id: Long,
    val values: List<String>
)