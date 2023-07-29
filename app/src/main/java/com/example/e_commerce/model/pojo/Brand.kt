package com.example.e_commerce.model.pojo

data class Brand(
    val admin_graphql_api_id: String,
    val body_html: String,
    val disjunctive: Boolean,
    val handle: String,
    val id: Long,
    val image: Image,
    val published_at: String,
    val published_scope: String,
    val rules: List<Rule>,
    val sort_order: String,
    val template_suffix: Any,
    val title: String,
    val updated_at: String
)