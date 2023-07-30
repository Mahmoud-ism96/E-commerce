package com.data.source

import com.example.e_commerce.services.db.LocalSource

class FakeLocaleSource(
    private var stringReadValue: String
): LocalSource {
    override fun writeStringToSettingSP(key: String, value: String) {
        stringReadValue = value
    }

    override fun readStringFromSettingSP(key: String): String {
        return stringReadValue
    }
}