package com.kerdus.gqasir.data.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


data class ProdukResponse(
	@field:SerializedName("ProdukResponse")
	val produkResponse: List<ProdukResponseItem>
)
@Parcelize
data class ProdukResponseItem(
	@field:SerializedName("stock_gudang")
	val stockGudang: Int,

	@field:SerializedName("price")
	val price: Int,

	@field:SerializedName("stock_kantin")
	val stockKantin: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("created_at")
	val createdAt: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("category")
	val category: String
): Parcelable
