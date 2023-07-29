package com.example.e_commerce.model.pojo.draftorder.response

import com.example.e_commerce.model.pojo.customer_resposnse.Customer

data class DraftOrder(
    val customer: Customer,
    val email: String,
    val id: Long,
    val line_items: List<LineItem>,
    val note: String
)