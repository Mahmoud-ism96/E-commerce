package com.example.e_commerce.model.pojo.order

data class DiscountCode(
    val code: String,
    val amount: String,
    val type:String="percentage"
)
