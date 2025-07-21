package com.kerdus.gqasir.data.api.request

data class ProdukUpdateRequest(
    val name: String,
    val category: String,
    val price: Int,
    val stock_gudang: Int,
    val stock_kantin: Int
)
