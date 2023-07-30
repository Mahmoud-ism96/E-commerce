package com.example.e_commerce.services.db

import android.content.Context
import com.example.e_commerce.model.pojo.CartItem
import com.example.e_commerce.services.settingsharedpreference.SettingSharedPref

class ConcreteLocalSource private constructor(context: Context): LocalSource {

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

    override fun writeStringToSettingSP(key: String, value: String) {
        settingSharedPref.writeStringToSettingSP(key, value)
    }

    override fun readStringFromSettingSP(key: String): String {
        return settingSharedPref.readStringFromSettingSP(key)
    }
}