package com.example.e_commerce.services.db

import android.content.Context
import com.example.e_commerce.model.pojo.CartItem
import com.example.e_commerce.services.settingsharedpreference.SettingSharedPref
import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource private constructor(context: Context): LocalSource {

    private val cartDao: CartDao by lazy {
        CartDatabase.getInstance(context).getDao()
    }

    private val settingSharedPref : SettingSharedPref by lazy {
        SettingSharedPref.getInstance(context)
    }

    companion object{
        private var instance: ConcreteLocalSource? = null

        fun getInstance(context: Context): ConcreteLocalSource{
            return instance?: ConcreteLocalSource(context).also {
                instance = it
            }
        }
    }

    override suspend fun insertItem(item: CartItem) {
        cartDao.insertItem(item)
    }

    override suspend fun deleteItem(item: CartItem) {
        cartDao.deleteItem(item)
    }

    override suspend fun updateQuantity(itemId: Long, newQuantity: Int) {
        cartDao.updateQuantity(itemId, newQuantity)
    }

    override fun getAllCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllCartItems()
    }

    override suspend fun deleteItemById(itemId: Long) {
        cartDao.deleteItemById(itemId)
    }

    override fun writeStringToSettingSP(key: String, value: String) {
        settingSharedPref.writeStringToSettingSP(key, value)
    }

    override fun readStringFromSettingSP(key: String): String {
        return settingSharedPref.readStringFromSettingSP(key)
    }
}