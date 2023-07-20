package com.example.e_commerce.services.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.e_commerce.model.pojo.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item: CartItem)

    @Delete
    suspend fun deleteItem(item: CartItem)

    @Query("DELETE FROM cart_item WHERE id = :itemId")
    suspend fun deleteItemById(itemId: Long)

    @Query("UPDATE cart_item SET quantity = :newQuantity WHERE id = :itemId")
    suspend fun updateQuantity( itemId:Long, newQuantity: Int)

    @Query("SELECT * FROM cart_item")
    fun getAllCartItems(): Flow<List<CartItem>>

}