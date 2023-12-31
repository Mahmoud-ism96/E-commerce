package com.example.e_commerce.services.db

import com.example.e_commerce.model.pojo.CartItem

interface LocalSource {
    fun writeStringToSettingSP(key: String, value: String)
    fun readStringFromSettingSP(key: String): String
}