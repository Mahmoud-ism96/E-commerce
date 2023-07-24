package com.example.e_commerce.model.pojo.order

data class Order(
    val email: String,
    val line_items: List<LineItem>,
    val send_receipt: Boolean = true,
    val inventory_behaviour: String = "decrement_obeying_policy",
)