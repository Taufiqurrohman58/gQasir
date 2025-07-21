package com.kerdus.gqasir.data.api.request

data class TransaksiRequest(
    val item_id: Int,
    val name: String,
    val quantity: Int,
    val price: Int
)
