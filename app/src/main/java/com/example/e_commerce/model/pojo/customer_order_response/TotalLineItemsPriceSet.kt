package com.example.e_commerce.model.pojo.customer_order_response

data class TotalLineItemsPriceSet(
    val presentment_money: PresentmentMoney,
    val shop_money: ShopMoney
)