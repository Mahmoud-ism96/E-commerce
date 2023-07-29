package com.example.e_commerce.model.pojo.customer

data class Customer(
    val addresses: List<Addresse> = listOf(),
    val email: String,
    val first_name: String,
    val last_name: String = "",
    val phone: String = "",
    val verified_email: Boolean = true,
    val note: String = "",
    val tags: String = ""
)