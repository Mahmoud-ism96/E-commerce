package com.example.e_commerce.model.pojo

data class Review(
    val reviewImage: String?,
    val reviewerName: String,
    val rating: Float,
    val reviewDetails: String?,
    val reviewDate: String
)