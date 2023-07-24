package com.example.e_commerce.services.settingsharedpreference

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce.utility.Constants

class SettingSharedPref private constructor(context: Context): SettingSPInterface {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(Constants.SETTING, AppCompatActivity.MODE_PRIVATE)
    }

    companion object{
        private var instance: SettingSharedPref? = null

        fun getInstance(context: Context): SettingSharedPref{
            return instance?: synchronized(this){
                instance?: SettingSharedPref(context).also { instance = it }
            }
        }
    }


    override fun writeStringToSettingSP(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun readStringFromSettingSP(key: String): String {
        var value :String
        sharedPreferences.getString(key, "null").let {
            value = it ?: "null"
        }
        return value
    }
}