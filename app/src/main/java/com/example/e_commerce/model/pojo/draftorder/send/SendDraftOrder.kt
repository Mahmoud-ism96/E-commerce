package com.example.e_commerce.model.pojo.draftorder.send

data class SendDraftOrder(
    val line_items: List<SendLineItem>?,
    val email: String,
    val note: String
)
