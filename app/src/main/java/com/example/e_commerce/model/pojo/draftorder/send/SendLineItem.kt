package com.example.e_commerce.model.pojo.draftorder.send

data class SendLineItem(
    val variant_id: Long,
    val quantity: Int,
    val properties: List<Property>,
)
