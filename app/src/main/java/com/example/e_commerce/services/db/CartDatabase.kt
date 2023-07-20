package com.example.e_commerce.services.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.e_commerce.model.pojo.CartItem

@Database(entities = [CartItem::class], version = 1)
abstract class CartDatabase: RoomDatabase() {

    abstract fun getDao(): CartDao

    companion object{
        private var instance: CartDatabase? = null

        fun getInstance(context: Context): CartDatabase{
            return instance?: synchronized(this){
                instance?: Room.databaseBuilder(context.applicationContext,CartDatabase::class.java,"cart_db")
                    .build().also { instance = it }
            }
        }
    }
}