package com.example.e_commerce.model.pojo.customer_modified_response

data class CustomerResponseData(
    val email: String,
    val firstName: String,
    val lastName: String,
    val note: String,
    val phone: String,
    val addresses: List<String>,
    val tags: String,
    val verifiedEmail: Boolean
)
