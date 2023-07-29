package com.example.e_commerce.model.pojo

import com.example.e_commerce.model.pojo.draftorder.response.LineItem
import java.io.Serializable

data class CheckArgument(
    val lineItems: List<LineItem>
): Serializable
