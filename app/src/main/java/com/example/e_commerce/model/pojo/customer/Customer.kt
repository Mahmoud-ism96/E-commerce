package com.example.e_commerce.model.pojo.customer

data class Customer(
    val addresses: List<Addresse> = listOf(
        Addresse(
            "123 Oak St", "Ottawa", "CA", "Mother", "Lastnameson", "+15142546011", "ON", "123 ABC"
        )
    ),
    val email: String,
    val first_name: String,
    val last_name: String = "ABC",
    val phone: String = "" ,
    val verified_email: Boolean = true
)