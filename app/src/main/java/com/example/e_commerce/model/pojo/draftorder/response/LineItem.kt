package com.example.e_commerce.model.pojo.draftorder.response

import com.example.e_commerce.model.pojo.draftorder.send.Property
import java.io.Serializable

data class LineItem(
    val id: Long,
    val price: String,
    val product_id: Long,
    var quantity: Int,
    val title: String,
    val variant_id: Long,
    val variant_title: String,
    val vendor: String,
    val properties: List<Property>
): Serializable