package com.example.e_commerce.services.db

import com.example.e_commerce.model.pojo.CartItem
import kotlinx.coroutines.flow.Flow

interface LocalSource {
    suspend fun insertItem(item: CartItem)
    suspend fun deleteItem(item: CartItem)
    suspend fun updateQuantity( itemId:Long, newQuantity: Int)
    fun getAllCartItems(): Flow<List<CartItem>>
    suspend fun deleteItemById(itemId: Long)
    fun writeStringToSettingSP(key: String, value: String)
    fun readStringFromSettingSP(key: String): String
}