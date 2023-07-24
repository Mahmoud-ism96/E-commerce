package com.example.e_commerce.services.settingsharedpreference

interface SettingSPInterface {
    fun writeStringToSettingSP(key: String, value: String)
    fun readStringFromSettingSP(key: String): String
}