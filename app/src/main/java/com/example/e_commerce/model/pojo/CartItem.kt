package com.example.e_commerce.model.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_item")
data class CartItem(
    @PrimaryKey
    val id: Long,
    val title: String,
    val price: String,
    val inventoryQuantity: Int,
    val imageSrc: String,
    val quantity: Int = 1,
)
